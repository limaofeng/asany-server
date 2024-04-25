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
package cn.asany.storage.core.engine.disk;

import cn.asany.storage.api.*;
import cn.asany.storage.core.AbstractFileObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.asany.jfantasy.framework.error.IgnoreException;

/**
 * 本地文件对象
 *
 * @author limaofeng
 */
public class LocalFileObject extends AbstractFileObject {

  private File file;
  private LocalStorage storage;

  public LocalFileObject(LocalStorage storage, File file) {
    super(
        file.getAbsolutePath()
            .substring(
                storage.defaultDir.length()
                    + (storage.defaultDir.endsWith(File.separator) ? -1 : 0)));
    this.file = file;
    this.storage = storage;
  }

  public LocalFileObject(LocalStorage storage, final File file, FileObjectMetadata metadata) {
    super(
        file.getAbsolutePath()
            .substring(
                storage.defaultDir.length()
                    + (storage.defaultDir.endsWith(File.separator) ? -1 : 0)),
        file.length(),
        new Date(file.lastModified()),
        metadata);
    this.file = file;
    this.storage = storage;
  }

  @Override
  public FileObject getParentFile() {
    return this.storage.retrieveFileItem(file.getParentFile());
  }

  @Override
  public List<FileObject> listFiles() {
    List<FileObject> fileObjects = new ArrayList<>();
    if (!this.isDirectory()) {
      return fileObjects;
    }
    for (File f :
        Objects.requireNonNull(
            file.listFiles(
                pathname ->
                    (pathname.isDirectory() || pathname.isFile()) && !pathname.isHidden()))) {
      fileObjects.add(this.storage.retrieveFileItem(f));
    }
    return fileObjects;
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
  public List<FileObject> listFiles(FileItemSelector selector) {
    if (!this.isDirectory()) {
      return new ArrayList<>();
    }
    return FileObject.Util.flat(this.listFiles(), selector);
  }

  @Override
  public Storage getStorage() {
    return this.storage;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    if (this.isDirectory()) {
      throw new IgnoreException("当前对象为一个目录,不能获取 InputStream ");
    }
    return this.storage.readFile(this.getPath());
  }
}
