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
package cn.asany.storage.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import net.asany.jfantasy.framework.util.common.StringUtil;

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
