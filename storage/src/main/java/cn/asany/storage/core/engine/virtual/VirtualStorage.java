package cn.asany.storage.core.engine.virtual;

import cn.asany.storage.api.*;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.domain.Space;
import cn.asany.storage.data.service.FileService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.framework.util.common.file.FileUtil;

public class VirtualStorage implements Storage {

  private final FileService fileService;
  private final Space space;
  private final VirtualFileObject rootFolder;
  private final StorageResolver storageResolver;

  public VirtualStorage(Space space, StorageResolver storageResolver, FileService fileService) {
    this.space = space;
    this.storageResolver = storageResolver;
    this.rootFolder = (VirtualFileObject) space.getVFolder().toFileObject(this);
    this.fileService = fileService;
  }

  @Override
  public String getId() {
    return "virtual";
  }

  public void writeFile(String remotePath, File file, String name) throws IOException {
    String path = getFullpath(remotePath);
    FileDetail parentFolder = fileService.getOneByPath(path.replaceFirst("[^/]+$", ""));
    String filename = path.substring(parentFolder.getPath().length());
    Optional<FileDetail> fileDetailOptional = fileService.findByPath(path);
    String showName = StringUtil.defaultValue(name, filename);
    FileDetail fileDetail =
        fileDetailOptional.orElseGet(
            () -> {
              String mimeType = FileUtil.getMimeType(file);
              String storePath = parentFolder.getStorePath() + filename;
              long size = file.length();
              return fileService.createFile(
                  path,
                  showName,
                  "application/zip",
                  size,
                  "",
                  parentFolder.getStorageConfig().getId(),
                  storePath,
                  "",
                  parentFolder.getId());
            });

    Storage innerStorage = this.storageResolver.resolve(fileDetail.getStorageConfig().getId());

    innerStorage.writeFile(fileDetail.getStorePath(), file);

    FileObject storeFile = innerStorage.getFileItem(fileDetail.getStorePath());

    fileDetail.setName(showName);
    fileDetail.setMd5(storeFile.getMetadata().getETag());

    this.fileService.update(fileDetail);
  }

  @Override
  public void writeFile(String remotePath, File file) throws IOException {
    this.writeFile(remotePath, file, null);
  }

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
    return getRealFileItem(remotePath).getInputStream();
  }

  @Override
  public List<FileObject> listFiles() {
    List<FileDetail> objects =
        this.fileService.findAll(
            PropertyFilter.newFilter().equal("parentFile.id", this.rootFolder.getId()));
    return objects.stream().map(item -> item.toFileObject(this)).collect(Collectors.toList());
  }

  @Override
  public List<FileObject> listFiles(String remotePath) {
    List<FileDetail> objects =
        this.fileService.findAll(PropertyFilter.newFilter().equal("parentFile.path", remotePath));
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

  public String getFullpath(String remotePath) {
    return this.getRootFolder().getOriginalPath() + remotePath.replaceFirst("^/", "");
  }

  public FileObject getRealFileItem(String remotePath) {
    Optional<FileDetail> fileDetailOptional = this.fileService.findByPath(getFullpath(remotePath));
    FileDetail fileDetail =
        fileDetailOptional.orElseThrow(() -> new RuntimeException("文件[" + remotePath + "]不存在"));
    if (fileDetail.getIsDirectory()) {
      throw new RuntimeException("只能访问文件");
    }
    return this.storageResolver
        .resolve(fileDetail.getStorageConfig().getId())
        .getFileItem(fileDetail.getStorePath());
  }

  @Override
  public FileObject getFileItem(String remotePath) {
    if (FileObject.ROOT_PATH.equals(remotePath)) {
      return null;
    }
    String path = getFullpath(remotePath);
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

  public String getNamePath(VirtualFileObject file) {
    List<VirtualFileObject> parents = new ArrayList<>();
    VirtualFileObject parent = (VirtualFileObject) file.getParentFile();
    parents.add(0, file);
    while (parent != null) {
      if (parent.getId().equals(getRootFolder().getId())) {
        break;
      }
      parents.add(0, parent);
      parent = (VirtualFileObject) parent.getParentFile();
    }
    return FileObject.SEPARATOR
        + parents.stream().map(FileObject::getName).collect(Collectors.joining("/"))
        + (file.isDirectory() ? FileObject.SEPARATOR : "");
  }
}
