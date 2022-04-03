package cn.asany.storage.core.engine.virtual;

import cn.asany.storage.api.*;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.FileService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

public class VirtualStorage implements Storage {

  private final FileService fileService;
  private final Space space;

  private final Storage storage;
  private boolean alias = false;
  private VirtualFileObject rootFolder;

  public VirtualStorage(Space space, Storage storage, FileService fileService) {
    this.storage = storage;
    this.space = space;
    this.rootFolder = (VirtualFileObject) space.getVFolder().toFileObject(this);
    this.fileService = fileService;
  }

  public VirtualStorage(Space space, Storage storage, FileService fileService, boolean alias) {
    this.storage = storage;
    this.space = space;
    this.fileService = fileService;
    this.alias = alias;
  }

  @Override
  public String getId() {
    return "virtual";
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
    return this.storage.readFile(remotePath);
  }

  @Override
  public List<FileObject> listFiles() {
    List<FileDetail> objects =
        this.fileService.findAll(
            PropertyFilter.builder().equal("parentFile.id", this.rootFolder.getId()).build());
    return objects.stream().map(item -> item.toFileObject(this)).collect(Collectors.toList());
  }

  @Override
  public List<FileObject> listFiles(String remotePath) {
    List<FileDetail> objects =
        this.fileService.findAll(
            PropertyFilter.builder().equal("parentFile.path", remotePath).build());
    return objects.stream().map(item -> item.toFileObject(this)).collect(Collectors.toList());
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
    if (FileObject.ROOT_PATH.equals(remotePath)) {
      return null;
    }
    String path = this.getRootFolder().getOriginalPath() + remotePath.replaceFirst("^/", "");
    return this.fileService.findByPath(path).map(item -> item.toFileObject(this)).orElse(null);
  }

  @Override
  public void removeFile(String remotePath) {}

  public StorageSpace getSpace() {
    return this.space;
  }

  public String getRootPath() {
    if (this.space == null) {
      throw new RuntimeException("space is null");
    }
    return this.space.getVFolder().getPath();
  }

  public VirtualFileObject getRootFolder() {
    return this.rootFolder;
  }

  public Storage getInnerStorage() {
    return storage;
  }

  public String convertPath(VirtualFileObject file) {
    if (this.alias) {
      List<VirtualFileObject> parents = new ArrayList<>();
      VirtualFileObject parent = (VirtualFileObject) file.getParentFile();
      parents.add(0, file);
      while (parent != null) {
        if (parent.getOriginalPath().equals(getRootPath())) {
          parent = null;
          continue;
        }
        parents.add(0, parent);
        parent = (VirtualFileObject) parent.getParentFile();
      }
      return FileObject.SEPARATOR
          + parents.stream().map(FileObject::getName).collect(Collectors.joining("/"))
          + (file.isDirectory() ? FileObject.SEPARATOR : "");
    }
    return file.getOriginalPath().substring(getRootPath().length() - 1);
  }
}
