package cn.asany.storage.core.engine.disk;

import cn.asany.storage.api.FileItemFilter;
import cn.asany.storage.api.FileItemSelector;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.FileObjectMetadata;
import cn.asany.storage.core.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jfantasy.framework.error.IgnoreException;

public class LocalFileObject extends AbstractFileObject {

  private File file;
  private LocalStorage fileManager;

  LocalFileObject(LocalStorage fileManager, File file) {
    super(
        file.getAbsolutePath()
            .substring(
                fileManager.defaultDir.length()
                    + (fileManager.defaultDir.endsWith(File.separator) ? -1 : 0)));
    this.file = file;
    this.fileManager = fileManager;
  }

  LocalFileObject(LocalStorage fileManager, final File file, FileObjectMetadata metadata) {
    super(
        file.getAbsolutePath()
            .substring(
                fileManager.defaultDir.length()
                    + (fileManager.defaultDir.endsWith(File.separator) ? -1 : 0)),
        file.length(),
        new Date(file.lastModified()),
        metadata);
    this.file = file;
    this.fileManager = fileManager;
  }

  @Override
  public FileObject getParentFile() {
    return this.fileManager.retrieveFileItem(file.getParentFile());
  }

  @Override
  public List<FileObject> listFiles() {
    List<FileObject> fileObjects = new ArrayList<>();
    if (!this.isDirectory()) {
      return fileObjects;
    }
    for (File f :
        file.listFiles(
            pathname -> (pathname.isDirectory() || pathname.isFile()) && !pathname.isHidden())) {
      fileObjects.add(this.fileManager.retrieveFileItem(f));
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
  public InputStream getInputStream() throws IOException {
    if (this.isDirectory()) {
      throw new IgnoreException("当前对象为一个目录,不能获取 InputStream ");
    }
    return this.fileManager.readFile(this.getAbsolutePath());
  }
}
