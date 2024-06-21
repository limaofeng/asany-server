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
package cn.asany.storage.core.engine.ftp;

import cn.asany.storage.api.FileItemFilter;
import cn.asany.storage.api.FileItemSelector;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.error.IgnoreException;
import net.asany.jfantasy.framework.service.FTPService;
import net.asany.jfantasy.framework.util.regexp.RegexpUtil;
import org.apache.commons.net.ftp.FTPFile;

@Slf4j
public class FTPStorage implements Storage {
  private final String id;
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
    writeFile(absolutePath, Files.newInputStream(file.toPath()));
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
      log.error(e.getMessage(), e);
    }
  }
}
