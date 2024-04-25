/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.storage.data.graphql.directive;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.service.AuthToken;
import cn.asany.storage.data.service.AuthTokenService;
import cn.asany.storage.data.util.IdUtils;
import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.SpringSecurityUtils;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2AccessToken;
import net.asany.jfantasy.framework.security.authentication.Authentication;
import net.asany.jfantasy.framework.util.common.StreamUtil;
import net.asany.jfantasy.framework.util.ognl.OgnlUtil;
import net.asany.jfantasy.framework.util.web.WebUtil;
import net.asany.jfantasy.graphql.security.context.GraphQLContextHolder;
import org.apache.commons.net.util.Base64;

/**
 * 文件对象格式指令
 *
 * @author limaofeng
 */
@Slf4j
public class FileFormatDirective implements SchemaDirectiveWiring {

  private static final String DIRECTIVE_NAME = "FileFormat";
  private static final String FORMAT_NAME = "format";
  private static final String FORMAT_BASE64 = "base64";
  private static final String FORMAT_URL = "url";
  private static final String FORMAT_PATH = "path";

  private static final String FORMAT_ID = "id";

  private static final GraphQLArgument.Builder FORMAT_ARGUMENT =
      GraphQLArgument.newArgument()
          .name(FORMAT_NAME)
          .type(
              GraphQLEnumType.newEnum()
                  .name(DIRECTIVE_NAME)
                  .description("文件自定在格式")
                  .value(FORMAT_BASE64, FORMAT_BASE64, "仅支持图片")
                  .value(FORMAT_URL, FORMAT_URL, "自动添加上域名")
                  .value(FORMAT_PATH, FORMAT_PATH, "只返回 PATH")
                  .value(FORMAT_ID, FORMAT_ID, "只返回 ID")
                  .build())
          .description("文件自定在格式");

  private final AuthTokenService authTokenService;
  private final StorageResolver storageResolver;

  public FileFormatDirective(AuthTokenService authTokenService, StorageResolver storageResolver) {
    this.authTokenService = authTokenService;
    this.storageResolver = storageResolver;
  }

  @Override
  public GraphQLFieldDefinition onField(
      SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
    GraphQLFieldDefinition field = environment.getElement();
    GraphQLFieldsContainer parentType = environment.getFieldsContainer();

    DataFetcher<?> originalDataFetcher =
        environment.getCodeRegistry().getDataFetcher(parentType, field);
    DataFetcher<?> dataFetcher =
        DataFetcherFactories.wrapDataFetcher(
            originalDataFetcher,
            (dataFetchingEnvironment, value) -> {
              String format = dataFetchingEnvironment.getArgument(FORMAT_NAME);
              if (value instanceof FileObject && format != null) {
                return buildFileObject((FileObject) value, format);
              }
              return value;
            });

    environment.getCodeRegistry().dataFetcher(parentType, field, dataFetcher);
    return field.transform(builder -> builder.argument(FORMAT_ARGUMENT));
  }

  private String getTokenValue(String path) {
    Authentication authentication = SpringSecurityUtils.getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      OAuth2AccessToken credentials = (OAuth2AccessToken) authentication.getCredentials();
      LoginUser user = authentication.getPrincipal();
      String token =
          this.authTokenService.storeToken(
              AuthToken.builder()
                  .path(path)
                  .user(user.getUid())
                  .personalToken(credentials.getTokenValue())
                  .build());
      return "?token=" + token;
    }
    return "";
  }

  private String buildFileObject(FileObject fileObject, String format) {
    String path = fileObject.getPath();
    if (FORMAT_BASE64.equals(format)) {
      return getImageBase64ByFileObject(fileObject);
    }
    if (FORMAT_URL.equals(format)) {
      String id = OgnlUtil.getInstance().getValue("id", fileObject);
      HttpServletRequest request = GraphQLContextHolder.getContext().getRequest();
      return WebUtil.getFullUrl(request, "/preview/" + id + getTokenValue(fileObject.getPath()));
    }
    if (FORMAT_ID.equals(format)) {
      return OgnlUtil.getInstance().getValue("id", fileObject);
    }
    return path;
  }

  /** 通过FileObject获取图片base64位编码 */
  public String getImageBase64ByFileObject(FileObject fileObject) {
    if (fileObject.getSize() > (1024 * 1014 * 5L)) {
      return null;
    }
    if (!"image".equals(fileObject.getMimeType().substring(0, 5))) {
      return null;
    }
    try {
      return "data:" + fileObject.getMimeType() + ";base64," + imageToBase64ByOnline(fileObject);
    } catch (IOException e) {
      log.error(e.getMessage());
      return null;
    }
  }

  /**
   * 在线图片转换成base64字符串
   *
   * @param file 图片线上路径
   * @author limaofeng
   * @return String
   */
  public String imageToBase64ByOnline(FileObject file) throws IOException {
    String id = OgnlUtil.getInstance().getValue("id", file);
    IdUtils.FileKey fileKey = IdUtils.parseKey(id);
    FileDetail fileDetail = fileKey.getFile();
    Storage storage = storageResolver.resolve(fileDetail.getStorageConfig().getId());
    // 创建链接
    InputStream is = storage.readFile(fileDetail.getStorePath());
    ByteArrayOutputStream data = new ByteArrayOutputStream();
    // 将内容读取内存中
    StreamUtil.copyThenClose(is, data);
    // 对字节数组Base64编码
    return new String(Base64.encodeBase64(data.toByteArray()));
  }
}
