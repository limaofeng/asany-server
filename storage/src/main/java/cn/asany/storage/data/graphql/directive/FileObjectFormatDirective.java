package cn.asany.storage.data.graphql.directive;

import cn.asany.storage.api.FileObject;
import graphql.Scalars;
import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.Base64;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;

/**
 * 文件对象格式指令
 *
 * @author fengmeng
 * @date 2019/5/7 20:45
 */
@Slf4j
public class FileObjectFormatDirective implements SchemaDirectiveWiring {
  private static final String DIRECTIVE_NAME = "FileObjectFormat";
  private static final String FORMAT_NAME = "format";
  private static final String FORMAT_BASE64 = "base64";
  private static final String FORMAT_URL = "url";
  private static final String FORMAT_PATH = "path";

  @Override
  public GraphQLFieldDefinition onField(
      SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
    GraphQLFieldDefinition field = environment.getElement();
    GraphQLFieldsContainer parentType = environment.getFieldsContainer();
    DataFetcher<?> originalDataFetcher =
        environment.getCodeRegistry().getDataFetcher(parentType, field);
    DataFetcher<?> dataFetcher =
        dataFetchingEnvironment -> {
          Object value = originalDataFetcher.get(dataFetchingEnvironment);
          String format = dataFetchingEnvironment.getArgument(FORMAT_NAME);
          if (value instanceof FileObject && format != null) {
            return buildFileObject((FileObject) value, format);
          }
          return value;
        };

    GraphQLArgument.Builder formatArgument =
        GraphQLArgument.newArgument()
            .name(FORMAT_NAME)
            .type(Scalars.GraphQLString)
            .type(
                GraphQLEnumType.newEnum()
                    .name(DIRECTIVE_NAME)
                    .description("文件自定在格式")
                    .value(FORMAT_BASE64, FORMAT_BASE64, "仅支持图片")
                    .value(FORMAT_URL, FORMAT_URL, "自动添加上域名")
                    .value(FORMAT_PATH, FORMAT_PATH, "只返回 PATH")
                    .build())
            .description("文件自定在格式");

    environment.getCodeRegistry().dataFetcher(parentType, field, dataFetcher);
    return field.transform(builder -> builder.argument(formatArgument));
  }

  private String buildFileObject(FileObject fileObject, String format) {
    String path = fileObject.getPath();
    if (FORMAT_BASE64.equals(format)) {
      return getImageBase64ByFileObject(fileObject, path);
    }
    if (FORMAT_URL.equals(format)) {
      String url = OgnlUtil.getInstance().getValue("url", fileObject);
      return StringUtil.defaultValue(url, path);
    }
    return path;
  }

  /** 通过FileObject获取图片base64位编码 */
  public static String getImageBase64ByFileObject(FileObject fileObject, String url) {
    if (fileObject.getSize() > (1024 * 1014 * 5L)) {
      return null;
    }
    if (!"image".equals(fileObject.getMimeType().substring(0, 5))) {
      return null;
    }
    try {
      return "data:" + fileObject.getMimeType() + ";base64," + imageToBase64ByOnline(url);
    } catch (IOException e) {
      log.error(e.getMessage());
      return null;
    }
  }

  /**
   * 在线图片转换成base64字符串
   *
   * @param imgUrl 图片线上路径
   * @author ZHANGJL
   * @date 2018-02-23 14:43:18
   * @return String
   */
  public static String imageToBase64ByOnline(String imgUrl) throws IOException {
    ByteArrayOutputStream data = new ByteArrayOutputStream();
    // 创建URL
    URL url = new URL(imgUrl);
    byte[] by = new byte[1024];
    // 创建链接
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setConnectTimeout(5000);
    InputStream is = conn.getInputStream();
    // 将内容读取内存中
    int len;
    while ((len = is.read(by)) != -1) {
      data.write(by, 0, len);
    }
    // 关闭流
    is.close();
    // 对字节数组Base64编码
    return new String(Base64.encodeBase64(data.toByteArray()));
  }
}
