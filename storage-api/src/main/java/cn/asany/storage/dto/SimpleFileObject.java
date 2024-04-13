package cn.asany.storage.dto;

import cn.asany.storage.api.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.*;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认的对象, 用于穿梭或者数据序列化
 *
 * @author limaofeng
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleFileObject implements FileObject, Serializable {
  private String id;
  /** 文件名称 */
  private String name;
  /** 是否为目录 */
  private boolean directory;
  /** 文件类型 */
  private String mimeType;
  /** 文件长度 */
  private long size;
  /** 虚拟文件路径 */
  private String path;
  /** 完整地址 */
  @JsonIgnore private String url;

  private Storage storage;

  private FileObjectMetadata metadata;

  private Date lastModified;

  public SimpleFileObject(String path) {
    this.path = path;
    this.mimeType = URLConnection.guessContentTypeFromName(path);
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Override
  @JsonIgnore
  public boolean isDirectory() {
    return this.directory;
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
    return this.lastModified;
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
    if (this.metadata == null) {
      return this.metadata = FileObjectMetadata.builder().build();
    }
    return this.metadata;
  }

  @Override
  public Storage getStorage() {
    return this.storage;
  }

  @JsonIgnore
  @Override
  public InputStream getInputStream() throws IOException {
    return null;
  }

  public static class SimpleFileObjectBuilder {
    public SimpleFileObjectBuilder metadata(String md5) {
      this.metadata =
          FileObjectMetadata.builder()
              .contentLength(this.size)
              .dir(this.directory)
              .contentType(this.mimeType)
              .build();
      this.metadata.setETag(md5);
      return this;
    }

    public SimpleFileObjectBuilder addUserMetadata(String key, Object value) {
      this.metadata.addUserMetadata(key, value);
      return this;
    }
  }

  public static class SimpleStorage implements Storage {

    private final String id;

    public SimpleStorage(String id) {
      this.id = id;
    }

    @Override
    public String getId() {
      return this.id;
    }

    @Override
    public void writeFile(String remotePath, File file) throws IOException {}

    @Override
    public void writeFile(String remotePath, InputStream in) throws IOException {}

    @Override
    public OutputStream writeFile(String remotePath) throws IOException {
      return null;
    }

    @Override
    public void readFile(String remotePath, String localPath) throws IOException {}

    @Override
    public void readFile(String remotePath, OutputStream out) throws IOException {}

    @Override
    public InputStream readFile(String remotePath) throws IOException {
      return null;
    }

    @Override
    public List<FileObject> listFiles() {
      return null;
    }

    @Override
    public List<FileObject> listFiles(String remotePath) {
      return null;
    }

    @Override
    public List<FileObject> listFiles(FileItemSelector selector) {
      return null;
    }

    @Override
    public List<FileObject> listFiles(String remotePath, FileItemSelector selector) {
      return null;
    }

    @Override
    public List<FileObject> listFiles(FileItemFilter filter) {
      return null;
    }

    @Override
    public List<FileObject> listFiles(String remotePath, FileItemFilter filter) {
      return null;
    }

    @Override
    public FileObject getFileItem(String remotePath) {
      return null;
    }

    @Override
    public void removeFile(String remotePath) {}
  }
}
