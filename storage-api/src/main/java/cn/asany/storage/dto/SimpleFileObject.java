package cn.asany.storage.dto;

import cn.asany.storage.api.FileItemFilter;
import cn.asany.storage.api.FileItemSelector;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.FileObjectMetadata;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

/**
 * 默认的对象, 用于穿梭或者数据序列化
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
public class SimpleFileObject implements FileObject {
  private Long id;
  /** 文件名称 */
  private String name;
  /** 文件类型 */
  private String contentType;
  /** 文件长度 */
  private long size;
  /** 虚拟文件路径 */
  private String path;
  /** 完整地址 */
  private String url;

  public SimpleFileObject(String path) {
    this.path = path;
    this.contentType = URLConnection.guessContentTypeFromName(path);
  }

  public void setUrl(String url) {
    this.url = url;
    if (this.contentType == null) {
      this.contentType = URLConnection.guessContentTypeFromName(url);
    }
  }

  public void setPath(String path) {
    this.path = path;
    if (this.contentType == null) {
      this.contentType = URLConnection.guessContentTypeFromName(path);
    }
  }

  @Override
  @JsonIgnore
  public boolean isDirectory() {
    return false;
  }

  @Override
  @JsonIgnore
  public FileObject getParentFile() {
    return null;
  }

  @Override
  @JsonIgnore
  public List<FileObject> listFiles() {
    return null;
  }

  @Override
  @JsonIgnore
  public Date lastModified() {
    return null;
  }

  @Override
  public List<FileObject> listFiles(FileItemFilter filter) {
    return null;
  }

  @Override
  public List<FileObject> listFiles(FileItemSelector selector) {
    return null;
  }

  @Override
  @JsonIgnore
  public FileObjectMetadata getMetadata() {
    return null;
  }

  @Override
  @JsonIgnore
  public InputStream getInputStream() throws IOException {
    return null;
  }
}
