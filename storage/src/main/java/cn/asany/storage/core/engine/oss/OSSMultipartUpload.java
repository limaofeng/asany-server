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
package cn.asany.storage.core.engine.oss;

import cn.asany.storage.api.FileObjectMetadata;
import cn.asany.storage.api.IMultipartUpload;
import cn.asany.storage.api.UploadException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.util.regexp.RegexpUtil;

public class OSSMultipartUpload implements IMultipartUpload {

  private final OSSClient client;
  private final String bucketName;

  public OSSMultipartUpload(OSSClient client, String bucketName) {
    this.client = client;
    this.bucketName = bucketName;
  }

  @Override
  public String initiate(String remotePath, FileObjectMetadata objectMetadata) {
    String path = RegexpUtil.replace(remotePath, "^/", "");
    ObjectMetadata _objectMetadata = new ObjectMetadata();
    if (objectMetadata.hasMetadataByKey(FileObjectMetadata.CONTENT_TYPE)) {
      _objectMetadata.setContentType(objectMetadata.getContentType());
    }
    if (objectMetadata.hasMetadataByKey(FileObjectMetadata.CONTENT_LENGTH)) {
      _objectMetadata.setContentLength(objectMetadata.getContentLength());
    }
    InitiateMultipartUploadRequest request =
        new InitiateMultipartUploadRequest(bucketName, path, _objectMetadata);
    InitiateMultipartUploadResult result = this.client.initiateMultipartUpload(request);
    return result.getUploadId();
  }

  @Override
  public String uploadPart(
      String remotePath, String uploadId, int partNumber, File file, long partSize)
      throws UploadException {
    try {
      String path = RegexpUtil.replace(remotePath, "^/", "");
      UploadPartRequest request = new UploadPartRequest();
      request.setPartNumber(partNumber);
      request.setBucketName(this.bucketName);
      request.setUploadId(uploadId);
      request.setPartSize(partSize);
      request.setKey(path);
      request.setInputStream(new FileInputStream(file));
      UploadPartResult result = this.client.uploadPart(request);

      return result.getETag();
    } catch (IOException e) {
      throw new UploadException(e.getMessage());
    }
  }

  @Override
  public String complete(String remotePath, String uploadId, List<String> partETags) {
    String path = RegexpUtil.replace(remotePath, "^/", "");

    AtomicInteger i = new AtomicInteger();
    CompleteMultipartUploadRequest request =
        new CompleteMultipartUploadRequest(
            this.bucketName,
            path,
            uploadId,
            partETags.stream()
                .map((etag) -> new PartETag(i.incrementAndGet(), etag))
                .collect(Collectors.toList()));

    CompleteMultipartUploadResult result = this.client.completeMultipartUpload(request);

    return result.getETag();
  }

  @Override
  public void abort(String uploadId) {}
}
