package cn.asany.storage.core.engine.minio;

import cn.asany.storage.api.*;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import java.io.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.springframework.http.HttpStatus;

/**
 * MinIO 存储器
 *
 * @author limaofeng
 */
public class MinIOStorage implements Storage {

  private final String endpoint;
  private final String accessKeyId;
  private final String accessKeySecret;
  protected final String bucketName;
  private final boolean useSSL;
  private final String id;
  protected MinioClient client;

  public MinIOStorage(
      String id,
      String endpoint,
      String accessKeyId,
      String accessKeySecret,
      String bucketName,
      boolean useSSL) {
    this.id = id;
    this.accessKeyId = accessKeyId;
    this.accessKeySecret = accessKeySecret;
    this.bucketName = bucketName;
    this.endpoint = endpoint;
    this.useSSL = useSSL;
    this.client =
        MinioClient.builder()
            .endpoint((useSSL ? "https://" : "http://") + endpoint)
            .credentials(accessKeyId, accessKeySecret)
            .build();
  }

  @SneakyThrows
  @Override
  public void writeFile(String remotePath, File file) throws IOException {
    this.client.uploadObject(
        UploadObjectArgs.builder()
            .filename(file.getAbsolutePath())
            .object(path(remotePath))
            .bucket(this.bucketName)
            .build());
  }

  @SneakyThrows
  @Override
  public void writeFile(String remotePath, InputStream in) throws IOException {
    this.client.putObject(PutObjectArgs.builder().stream(in, 0, 0).bucket(this.bucketName).build());
  }

  @Override
  public OutputStream writeFile(String remotePath) throws IOException {
    throw new IOException(" OSS 不支持直接获取输入流对象操作文件,可以通过自定义 OutputStream 实现该方法!");
  }

  @Override
  public void readFile(String remotePath, String localPath) throws IOException {
    this.readFile(remotePath, new FileOutputStream(localPath));
  }

  @Override
  public void readFile(String remotePath, OutputStream out) throws IOException {
    StreamUtil.copyThenClose(readFile(remotePath), out);
  }

  @Override
  public InputStream readFile(String remotePath) throws IOException {
    FileObject fileObject = this.retrieveFileItem(remotePath);
    if (fileObject == null) {
      throw new FileNotFoundException("文件:" + remotePath + "不存在!");
    }
    return fileObject.getInputStream();
  }

  protected FileObject retrieveFileItem(String remotePath, Item item) {
    String absolutePath = RegexpUtil.replace(remotePath, "^/", "");
    FileObjectMetadata metadata = getObjectMetadata(absolutePath, item);
    return new MinIOFileObject(this, remotePath, metadata);
  }

  @SneakyThrows
  protected FileObject retrieveFileItem(String remotePath) {
    String absolutePath = RegexpUtil.replace(remotePath, "^/", "");
    if (StringUtil.isEmpty(absolutePath)) {
      return new MinIOFileObject(this, absolutePath, FileObjectMetadata.ROOT);
    }
    Optional<FileObjectMetadata> optional = getObjectMetadata(absolutePath);
    if (!optional.isPresent()) {
      return null;
    }
    FileObjectMetadata metadata = optional.get();
    return new MinIOFileObject(this, absolutePath, metadata);
  }

  @SneakyThrows
  private FileObjectMetadata getObjectMetadata(String absolutePath, Item item) {
    StatObjectResponse stat =
        client.statObject(
            StatObjectArgs.builder().bucket(this.bucketName).object(absolutePath).build());
    return FileObjectMetadata.builder()
        .contentLength(item.size())
        .etag(item.etag())
        .contentType(stat.contentType())
        .lastModified(Date.from(stat.lastModified().toInstant()))
        .dir(item.isDir())
        .build();
  }

  @SneakyThrows
  private Optional<FileObjectMetadata> getObjectMetadata(String absolutePath) {
    try {
      StatObjectResponse statObjectResponse =
          client.statObject(
              StatObjectArgs.builder().bucket(this.bucketName).object(absolutePath).build());
      return Optional.of(
          FileObjectMetadata.builder()
              .contentLength(statObjectResponse.size())
              .etag(statObjectResponse.etag())
              .contentType(statObjectResponse.contentType())
              .lastModified(Date.from(statObjectResponse.lastModified().toInstant()))
              .dir(absolutePath.endsWith("/") && statObjectResponse.size() == 0)
              .build());
    } catch (ErrorResponseException e) {
      if (e.response().code() == HttpStatus.NOT_FOUND.value()) {
        return Optional.empty();
      }
      throw e;
    }
  }

  @Override
  @SneakyThrows
  public List<FileObject> listFiles() {
    return listFiles("/");
  }

  @Override
  @SneakyThrows
  public List<FileObject> listFiles(String remotePath) {
    FileObject fileObject = retrieveFileItem(remotePath);
    return fileObject == null ? Collections.emptyList() : fileObject.listFiles();
  }

  @Override
  public List<FileObject> listFiles(FileItemSelector selector) {
    FileObject object = this.getFileItem("/");
    if (object == null) {
      return Collections.emptyList();
    }
    return object.listFiles(selector);
  }

  @Override
  public List<FileObject> listFiles(String remotePath, FileItemSelector selector) {
    FileObject object = this.getFileItem(remotePath);
    if (object == null) {
      return Collections.emptyList();
    }
    return object.listFiles(selector);
  }

  @Override
  public List<FileObject> listFiles(FileItemFilter filter) {
    FileObject object = this.getFileItem("/");
    if (object == null) {
      return Collections.emptyList();
    }
    return object.listFiles(filter);
  }

  @Override
  public List<FileObject> listFiles(String remotePath, FileItemFilter filter) {
    FileObject object = this.getFileItem(remotePath);
    if (object == null) {
      return Collections.emptyList();
    }
    return object.listFiles(filter);
  }

  @Override
  public FileObject getFileItem(String remotePath) {
    return this.retrieveFileItem(remotePath);
  }

  @Override
  public void removeFile(String remotePath) {}

  private String path(String remotePath) {
    return RegexpUtil.replace(remotePath, "^/", "");
  }

  @Override
  public String getId() {
    return id;
  }
}
