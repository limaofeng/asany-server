package cn.asany.storage.data.bean;

import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 上传ID */
  @Column(name = "UPLOAD_ID", length = 50, nullable = false)
  private String uploadId;
  /** 文件名称 */
  @Column(name = "NAME", length = 50, nullable = false)
  private String name;
  /** 文件唯一标识值 */
  @Column(name = "HASH", length = 100, nullable = false)
  private String hash;
  /** 存储路径 */
  @Column(name = "PATH", nullable = false, updatable = false, length = 250)
  private String path;
  /** 存储ID */
  @Column(name = "STORAGE_ID", nullable = false, updatable = false, length = 50)
  private String storage;
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
