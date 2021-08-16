package cn.asany.storage.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文件接口
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-8 下午4:49:25
 */
public interface FileObject {

  /**
   * 获取文件名
   *
   * @return string
   */
  String getName();

  /**
   * 是否为文件夹
   *
   * @return boolean
   */
  boolean isDirectory();

  /**
   * 文件大小
   *
   * @return long
   */
  long getSize();

  /**
   * 文件类型
   *
   * @return contentType
   */
  String getContentType();

  /**
   * 父文件
   *
   * @return FileItem
   */
  @JsonSerialize(using = ParentFileItemSerialize.class)
  FileObject getParentFile();

  /**
   * 子文件
   *
   * @return List<FileItem>
   */
  List<FileObject> listFiles();

  /**
   * 绝对路径
   *
   * @return string
   */
  String getAbsolutePath();

  /**
   * 最后修改日期
   *
   * @return Date
   */
  @JsonProperty("lastModified")
  Date lastModified();

  /**
   * 通过 FileItemFilter 接口 筛选文件,当前对象必须为文件夹，此方法有效
   *
   * @param filter 过滤器
   * @return List<FileItem>
   */
  @JsonIgnore
  List<FileObject> listFiles(FileItemFilter filter);

  /**
   * 通过 FileItemSelector 接口 筛选文件,当前对象必须为文件夹，此方法有效
   *
   * @param selector 选择器
   * @return List<FileItem>
   */
  @JsonIgnore
  List<FileObject> listFiles(FileItemSelector selector);

  /**
   * 获取 Metadata 信息
   *
   * @return List<FileItem>
   */
  FileObjectMetadata getMetadata();

  /**
   * 获取文件输入流
   *
   * @return InputStream
   * @throws IOException
   */
  @JsonIgnore
  InputStream getInputStream() throws IOException;

  class ParentFileItemSerialize extends JsonSerializer<FileObject> {

    @Override
    public void serialize(FileObject fileObject, JsonGenerator jgen, SerializerProvider provider)
        throws IOException {
      jgen.writeString(fileObject.getAbsolutePath());
    }
  }

  final class Util {
    private Util() {}

    public static List<FileObject> flat(List<FileObject> items, FileItemSelector selector) {
      List<FileObject> fileObjects = new ArrayList<FileObject>();
      for (FileObject item : items) {
        boolean include = selector.includeFile(item);
        if (include) {
          fileObjects.add(item);
        }
        if (include && item.isDirectory() && selector.traverseDescendents(item)) {
          fileObjects.addAll(flat(item.listFiles(), selector));
        }
      }
      return fileObjects;
    }
  }
}
