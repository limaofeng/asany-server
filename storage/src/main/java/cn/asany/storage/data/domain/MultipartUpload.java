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
package cn.asany.storage.data.domain;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(
    name = "STORAGE_MULTIPART_UPLOAD",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_MULTIPART_UPLOAD",
          columnNames = {"STORAGE_ID", "PATH"})
    })
@AllArgsConstructor
@Builder
public class MultipartUpload extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 上传ID */
  @Column(name = "UPLOAD_ID", length = 50, nullable = false)
  private String uploadId;

  /** 文件唯一标识值 */
  @Column(name = "HASH", length = 100, nullable = false)
  private String hash;

  /** 存储路径 */
  @Column(name = "PATH", nullable = false, updatable = false, length = 250)
  private String path;

  /** 存储空间 */
  @Column(name = "SPACE_ID", nullable = false, updatable = false)
  private String space;

  /** 存储ID */
  @Column(name = "STORAGE_ID", nullable = false, updatable = false, length = 50)
  private String storage;

  /** 文件类型 */
  @Column(name = "MIME_TYPE", length = 50, nullable = false)
  private String mimeType;

  /** 文件长度 */
  @Column(name = "SIZE", nullable = false)
  private Long size;

  /** 每段大小 */
  @Column(name = "CHUNK_SIZE")
  private Long chunkSize;

  /** 总的段数 */
  @Column(name = "CHUNK_LENGTH")
  private Integer chunkLength;

  /** 已上传的部分 */
  @Column(name = "UPLOADED_PARTS")
  private Integer uploadedParts;

  /** 所有已上传的文件片段 */
  @OneToMany(
      mappedBy = "upload",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @ToString.Exclude
  private List<MultipartUploadChunk> chunks;
}
