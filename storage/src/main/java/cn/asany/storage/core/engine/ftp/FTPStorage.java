package cn.asany.storage.core.engine.ftp;

import cn.asany.storage.api.FileItemFilter;
import cn.asany.storage.api.FileItemSelector;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import java.io.*;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPFile;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.service.FTPService;
import org.jfantasy.framework.util.regexp.RegexpUtil;

public class FTPStorage implements Storage {

  private static final Log LOGGER = LogFactory.getLog(FTPStorage.class);

  private String id;
  protected FTPService ftpService;

  public FTPStorage(String id, FTPService ftpService) {
    super();
    this.id = id;
    this.ftpService = ftpService;
  }

  public void setFtpService(FTPService ftpService) {
    this.ftpService = ftpService;
  }

  @Override
  public void readFile(String remotePath, String localPath) throws IOException {
    this.ftpService.download(remotePath, localPath);
  }

  @Override
  public void readFile(String remotePath, OutputStream out) throws IOException {
    this.ftpService.download(remotePath, out);
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void writeFile(String absolutePath, File file) throws IOException {
    writeFile(absolutePath, new FileInputStream(file));
  }

  @Override
  public void writeFile(String absolutePath, InputStream in) throws IOException {
    this.ftpService.uploadFile(in, absolutePath);
  }

  @Override
  public InputStream readFile(String remote) throws IOException {
    return this.ftpService.getInputStream(remote);
  }

  @Override
  public OutputStream writeFile(String remotePath) throws IOException {
    return this.ftpService.getOutputStream(remotePath);
  }

  protected FileObject retrieveFileItem(final FTPFile ftpFile, final String parentPath)
      throws IOException {
    return new FTPFileObject(ftpFile, parentPath, this);
  }

  @Override
  public FileObject getFileItem(String remotePath) {
    try {
      if (!this.ftpService.exist(remotePath)) {
        return null;
      }
      boolean dir = this.ftpService.isDir(remotePath);
      remotePath = dir ? remotePath.endsWith("/") ? remotePath : remotePath + "/" : remotePath;
      String parentPath = RegexpUtil.replace(remotePath, "[^/]+[/][^/]*$", "");
      if (dir) {
        return retrieveFileItem(
            this.ftpService.listFiles(RegexpUtil.replace(remotePath, "/$", ""))[0], parentPath);
      } else {
        return retrieveFileItem(this.ftpService.listFiles(remotePath)[0], parentPath);
      }
    } catch (IOException e) {
      throw new IgnoreException(e.getMessage(), e);
    }
  }

  @Override
  public List<FileObject> listFiles() {
    return null;
  }

  @Override
  public List<FileObject> listFiles(String remotePath) {
    return getFileItem(remotePath).listFiles();
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
  public void removeFile(String remotePath) {
    try {
      this.ftpService.deleteRemoteFile(remotePath);
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }
}
