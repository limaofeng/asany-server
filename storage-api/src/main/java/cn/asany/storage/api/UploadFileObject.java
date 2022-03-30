package cn.asany.storage.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import org.jfantasy.framework.util.common.StringUtil;

/**
 * 文件对象
 *
 * @author limaofeng
 */
public class UploadFileObject implements FileObject {
  @Getter private File file;
  @Getter private final String name;
  @Getter private FileObjectMetadata metadata;

  public UploadFileObject(String name, File file, FileObjectMetadata metadata) {
    this.name = name;
    this.file = file;
    this.metadata = metadata;
  }

  public UploadFileObject(String name, FileObjectMetadata metadata) {
    this.name = name;
    this.metadata = metadata;
  }

  public UploadFileObject(FileObjectMetadata metadata) {
    this.name = StringUtil.shortUUID();
    this.metadata = metadata;
  }

  public UploadFileObject(File file) {
    this.file = file;
    this.name = file.getName();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new FileInputStream(this.file);
  }

  @Override
  public Date lastModified() {
    return this.metadata.getLastModified();
  }

  @Override
  public long getSize() {
    return this.metadata.getContentLength();
  }

  @Override
  public String getMimeType() {
    return this.metadata.getContentType();
  }

  @Override
  public String getPath() {
    return this.file.getAbsolutePath();
  }

  @Override
  public boolean isDirectory() {
    return false;
  }

  @Override
  public FileObject getParentFile() {
    throw new RuntimeException("UploadFileObject 不允许访问该方法");
  }

  @Override
  public List<FileObject> listFiles() {
    throw new RuntimeException("UploadFileObject 不允许访问该方法");
  }

  @Override
  public List<FileObject> listFiles(FileItemFilter filter) {
    throw new RuntimeException("UploadFileObject 不允许访问该方法");
  }

  @Override
  public List<FileObject> listFiles(FileItemSelector selector) {
    throw new RuntimeException("UploadFileObject 不允许访问该方法");
  }

  @Override
  public Storage getStorage() {
    return null;
  }

  public boolean isNoFile() {
    return this.file == null;
  }
}
