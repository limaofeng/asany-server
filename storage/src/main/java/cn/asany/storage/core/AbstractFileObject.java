package cn.asany.storage.core;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.FileObjectMetadata;
import java.io.File;
import java.util.Date;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.framework.util.regexp.RegexpUtil;

/**
 * 抽象文件对象
 *
 * @author limaofeng
 */
public abstract class AbstractFileObject implements FileObject {
  private String absolutePath;
  private String name;
  private boolean directory;
  private Date lastModified;
  private FileObjectMetadata metadata;
  private long size;

  public AbstractFileObject(String absolutePath) {
    this.absolutePath = absolutePath.replace(File.separator, "/");
    if (!this.absolutePath.startsWith("/")) {
      this.absolutePath = "/" + this.absolutePath;
    }
    if (!this.absolutePath.endsWith("/")) {
      this.absolutePath = this.absolutePath + "/";
    }
    this.name = StringUtil.nullValue(RegexpUtil.parseGroup(this.absolutePath, "([^/]+)/$", 1));
    this.directory = true;
    this.lastModified = null;
    this.metadata = new FileObjectMetadata();
  }

  public AbstractFileObject(String absolutePath, FileObjectMetadata metadata) {
    this.absolutePath = absolutePath.replace(File.separator, "/");
    this.name = RegexpUtil.parseGroup(absolutePath, "([^/]+)/$", 1);
    this.directory = true;
    this.lastModified = null;
    this.metadata = metadata;
  }

  public AbstractFileObject(
      String absolutePath, long size, Date lastModified, FileObjectMetadata metadata) {
    this.absolutePath = absolutePath.replace(File.separator, "/");
    this.name = RegexpUtil.parseGroup(absolutePath, "([^/]+)$", 1);
    this.size = size;
    this.directory = false;
    this.lastModified = lastModified;
    this.metadata = metadata;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean isDirectory() {
    return this.directory;
  }

  @Override
  public long getSize() {
    return this.size;
  }

  @Override
  public String getMimeType() {
    return this.metadata.getContentType();
  }

  @Override
  public String getPath() {
    return this.absolutePath;
  }

  @Override
  public Date lastModified() {
    return this.lastModified;
  }

  @Override
  public FileObjectMetadata getMetadata() {
    return this.metadata;
  }
}
