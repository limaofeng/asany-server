package cn.asany.storage.core.engine.minio;

import cn.asany.storage.api.FileObjectMetadata;
import cn.asany.storage.api.IMultipartUpload;
import cn.asany.storage.api.UploadException;
import io.minio.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.util.common.StringUtil;

public class MinIOMultipartUpload implements IMultipartUpload {

  private final MinioClient client;
  private final String bucketName;

  public MinIOMultipartUpload(MinioClient client, String bucketName) {
    this.bucketName = bucketName;
    this.client = client;
  }

  @Override
  public String initiate(String remotePath, FileObjectMetadata objectMetadata) {
    return StringUtil.uuid();
  }

  @Override
  public String uploadPart(
      String remotePath, String uploadId, int partNumber, File file, long partSize)
      throws UploadException {
    try {
      ObjectWriteResponse response =
          this.client.uploadObject(
              UploadObjectArgs.builder()
                  .filename(file.getAbsolutePath(), partSize)
                  .object(MinIOStorage.path(remotePath))
                  .bucket(this.bucketName)
                  .build());
      return response.etag();
    } catch (Exception e) {
      throw new UploadException(e.getMessage(), e);
    }
  }

  @Override
  public String complete(String remotePath, String uploadId, List<String> partETags)
      throws UploadException {
    String path = MinIOStorage.path(remotePath);
    List<ComposeSource> sources =
        partETags.stream()
            .map(item -> ComposeSource.builder().bucket(bucketName).matchETag(item).build())
            .collect(Collectors.toList());

    ComposeObjectArgs args =
        ComposeObjectArgs.builder().bucket(this.bucketName).object(path).sources(sources).build();

    try {
      this.client.composeObject(args);
    } catch (Exception e) {
      throw new UploadException(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public void abort(String uploadId) {}
}
