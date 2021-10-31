package cn.asany.storage.core.engine.minio;

import cn.asany.storage.api.*;
import io.minio.ListObjectsArgs;
import io.minio.Result;
import io.minio.messages.Item;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

/**
 * 文件对象
 *
 * @author limaofeng
 */
public class MinIOFileObject implements FileObject {

  private final MinIOStorage storage;
  private final FileObjectMetadata metadata;
  private final String remotePath;

  public MinIOFileObject(MinIOStorage storage, String remotePath, FileObjectMetadata metadata) {
    this.storage = storage;
    this.metadata = metadata;
    this.remotePath = remotePath;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public boolean isDirectory() {
    return false;
  }

  @Override
  public long getSize() {
    return 0;
  }

  @Override
  public String getMimeType() {
    return null;
  }

  @Override
  public FileObject getParentFile() {
    return null;
  }

  @Override
  @SneakyThrows
  public List<FileObject> listFiles() {
    Iterable<Result<Item>> results =
        this.storage.client.listObjects(
            ListObjectsArgs.builder()
                .prefix(remotePath)
                .includeUserMetadata(true)
                .bucket(this.storage.bucketName)
                .build());
    List<FileObject> fileObjects = new ArrayList<>();
    for (Result<Item> itemResult : results) {
      Item item = itemResult.get();
      fileObjects.add(this.storage.retrieveFileItem(item.objectName(), item));
    }
    return fileObjects;
  }

  @Override
  public String getPath() {
    return this.remotePath;
  }

  @Override
  public Date lastModified() {
    return this.metadata.getLastModified();
  }

  @Override
  public List<FileObject> listFiles(FileItemFilter filter) {
    return this.listFiles().stream().filter(filter::accept).collect(Collectors.toList());
  }

  @Override
  public List<FileObject> listFiles(FileItemSelector selector) {
    return this.listFiles().stream().filter(selector::includeFile).collect(Collectors.toList());
  }

  @Override
  public FileObjectMetadata getMetadata() {
    return this.metadata;
  }

  @Override
  public Storage getStorage() {
    return this.storage;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return null;
  }
}
