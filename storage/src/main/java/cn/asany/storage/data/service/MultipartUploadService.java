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
package cn.asany.storage.data.service;

import cn.asany.storage.api.FileObjectMetadata;
import cn.asany.storage.data.dao.MultipartUploadChunkDao;
import cn.asany.storage.data.dao.MultipartUploadDao;
import cn.asany.storage.data.domain.MultipartUpload;
import cn.asany.storage.data.domain.MultipartUploadChunk;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MultipartUploadService {

  private final MultipartUploadChunkDao multipartUploadChunkDao;
  private final MultipartUploadDao multipartUploadDao;

  @Autowired
  public MultipartUploadService(
      MultipartUploadDao multipartUploadDao,
      MultipartUploadChunkDao multipartUploadChunkDao,
      FileService fileService) {
    this.multipartUploadDao = multipartUploadDao;
    this.multipartUploadChunkDao = multipartUploadChunkDao;
  }

  public List<MultipartUploadChunk> listUploadedParts(Long id) {
    return new ArrayList<>();
  }

  public MultipartUpload initiateMultipartUpload(
      String path,
      String space,
      String uploadId,
      String hash,
      String storage,
      long chunkSize,
      int chunkLength,
      FileObjectMetadata metadata) {
    return this.multipartUploadDao.save(
        MultipartUpload.builder()
            .uploadId(uploadId)
            .hash(hash)
            .path(path)
            .space(space)
            .storage(storage)
            .chunkSize(chunkSize)
            .chunkLength(chunkLength)
            .uploadedParts(0)
            .size(metadata.getContentLength())
            .mimeType(metadata.getContentType())
            .build());
  }

  public void abortMultipartUpload(Long id) {}

  public MultipartUploadChunk appendPart(
      Long uploadId, String path, String hash, Integer index, String etag, Long partSize) {
    MultipartUploadChunk chunk =
        MultipartUploadChunk.builder()
            .upload(MultipartUpload.builder().id(uploadId).build())
            .path(path)
            .hash(hash)
            .etag(etag)
            .index(index)
            .size(partSize)
            .build();
    return this.multipartUploadChunkDao.save(chunk);
  }

  public void delete(String path) {
    this.multipartUploadChunkDao.deleteById(path);
  }

  public MultipartUpload get(Long id) {
    return this.multipartUploadDao.getReferenceById(id);
  }

  public Optional<MultipartUpload> findMultipartUploadByHash(
      String hash, String space, String storage) {
    return this.multipartUploadDao.findOne(
        PropertyFilter.newFilter()
            .equal("hash", hash)
            .equal("space", space)
            .equal("storage", storage));
  }

  public Optional<MultipartUpload> findMultipartUploadByHash(String hash, String storage) {
    return this.multipartUploadDao.findOne(
        PropertyFilter.newFilter().equal("hash", hash).equal("storage", storage));
  }

  public void updateMultipartUpload(MultipartUpload multipartUpload) {
    this.multipartUploadDao.update(multipartUpload);
  }
}
