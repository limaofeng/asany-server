package cn.asany.storage.data.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File Metadata
 *
 * @author limaofeng
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata {
  /** 文件夹属性 - 允许的扩展名 */
  @Column(name = "ALLOWED_EXTENSIONS", length = 64)
  private String allowedExtensions;

  /** 文件夹属性 - 大小限制 */
  @Column(name = "LIMITS")
  private Long limits;

  /** 文件夹属性 - 允许子目录 */
  @Column(name = "ALLOWS_SUBDIRECTORY")
  private Boolean allowsSubdirectory;
}
