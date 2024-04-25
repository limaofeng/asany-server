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
package cn.asany.storage.core.engine.minio;

import cn.asany.storage.api.FileObjectMetadata;
import cn.asany.storage.api.IMultipartUpload;
import cn.asany.storage.api.UploadException;
import io.minio.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.util.common.StringUtil;

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
