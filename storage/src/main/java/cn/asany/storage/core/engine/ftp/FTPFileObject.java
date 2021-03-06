package cn.asany.storage.core.engine.ftp;

import cn.asany.storage.api.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPFile;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;

/**
 * FTP 文件类型
 *
 * @author limaofeng
 */
@Slf4j
public class FTPFileObject implements FileObject {
  private final FTPStorage storage;
  private final String parentPath;
  private final String absolutePath;
  private FTPFile ftpFile;

  public FTPFileObject(final FTPFile ftpFile, String parentPath, FTPStorage storage) {
    this.ftpFile = ftpFile;
    this.parentPath = parentPath;
    this.storage = storage;
    this.absolutePath =
        (parentPath.endsWith("/") ? parentPath : (parentPath + "/"))
            + (".".equals(ftpFile.getName()) ? "" : ftpFile.getName());
  }

  public FTPFileObject(String absolutePath, FTPStorage fileManager) {
    this.absolutePath = absolutePath;
    this.storage = fileManager;
    this.parentPath = RegexpUtil.replace(absolutePath, "[^/]+[/][^/]*$", "");
  }

  @Override
  public List<FileObject> listFiles(FileItemSelector selector) {
    if (!this.isDirectory()) {
      return new ArrayList<>();
    }
    return FileObject.Util.flat(this.listFiles(), selector);
  }

  @Override
  public FileObjectMetadata getMetadata() {
    return null;
  }

  @Override
  public Storage getStorage() {
    return this.storage;
  }

  @Override
  public List<FileObject> listFiles(FileItemFilter filter) {
    List<FileObject> fileObjects = new ArrayList<>();
    if (!this.isDirectory()) {
      return fileObjects;
    }
    for (FileObject item : listFiles()) {
      if (filter.accept(item)) {
        fileObjects.add(item);
      }
    }
    return fileObjects;
  }

  @Override
  public List<FileObject> listFiles() {
    try {
      List<FileObject> fileObjects = new ArrayList<>();
      if (!this.isDirectory()) {
        return fileObjects;
      }
      for (FTPFile ftpFile : storage.ftpService.listFiles(this.getPath() + "/")) {
        if (RegexpUtil.find(ftpFile.getName(), "^[.]{1,}$")) {
          continue;
        }
        fileObjects.add(storage.retrieveFileItem(ftpFile, this.getPath()));
      }
      return fileObjects;
    } catch (IOException e) {
      throw new IgnoreException(e.getMessage(), e);
    }
  }

  @Override
  public Date lastModified() {
    return getFtpFile().getTimestamp().getTime();
  }

  @Override
  public boolean isDirectory() {
    return getFtpFile().isDirectory();
  }

  @Override
  public long getSize() {
    return getFtpFile().getSize();
  }

  @Override
  public FileObject getParentFile() {
    return FileObject.ROOT_PATH.equals(this.getPath())
        ? null
        : new FTPFileObject(this.parentPath, storage);
  }

  @Override
  public String getName() {
    return getFtpFile().getName();
  }

  @Override
  public String getMimeType() {
    try {
      return FileUtil.getMimeType(getInputStream());
    } catch (IOException e) {
      log.error(" getContentType Error : ", e);
      return getFtpFile().getType() + "";
    }
  }

  @Override
  public String getPath() {
    return this.absolutePath;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    if (this.isDirectory()) {
      throw new IgnoreException("当前对象为一个目录,不能获取 InputStream ");
    }
    return storage.ftpService.getInputStream(getPath());
  }

  @JsonIgnore
  public FTPFile getFtpFile() {
    if (ftpFile == null) {
      try {
        ftpFile = storage.ftpService.listFiles(RegexpUtil.replace(this.absolutePath, "/$", ""))[0];
      } catch (IOException e) {
        throw new IgnoreException(e.getMessage(), e);
      }
    }
    return ftpFile;
  }
}
