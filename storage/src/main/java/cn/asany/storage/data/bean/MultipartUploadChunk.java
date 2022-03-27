package cn.asany.storage.data.bean;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.dto.SimpleFileObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 文件部分
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@Table(
    name = "STORAGE_MULTIPART_UPLOAD_CHUNK",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"UPLOAD_ID", "HASH"},
          name = "UK_MULTIPART_UPLOAD_CHUNK_HASH"),
      @UniqueConstraint(
          columnNames = {"UPLOAD_ID", "PAER_INDEX"},
          name = "UK_MULTIPART_UPLOAD_CHUNK_INDEX")
    })
public class MultipartUploadChunk extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 存储路径 */
  @Column(name = "PATH", nullable = false, updatable = false, length = 250)
  private String path;
  /** 文件长度 */
  @Column(name = "LENGTH")
  private Long size;
  /** 片段文件的hash值 */
  @Column(name = "HASH")
  private String hash;
  /** 当前段数 */
  @Column(name = "PAER_INDEX")
  private Integer index;
  /** 存储器中分片的唯一标识 */
  private String etag;
  /** 分段上传 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "UPLOAD_ID",
      foreignKey = @ForeignKey(name = "FK_STORAGE_MULTIPART_UPLOAD_CHUNK_UID"))
  @ToString.Exclude
  private MultipartUpload upload;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    MultipartUploadChunk chunk = (MultipartUploadChunk) o;
    return id != null && Objects.equals(id, chunk.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  public FileObject toFileObject() {
    return SimpleFileObject.builder()
        .name(this.path)
        .path(this.path)
        .size(this.size)
        .directory(false)
        .mimeType("application/octet-stream")
        .metadata(this.etag)
        .addUserMetadata("__ID", IdUtils.toKey("chunk", String.valueOf(this.id)))
        .addUserMetadata("__TYPE", "MultipartUploadChunk")
        .build();
  }
}
