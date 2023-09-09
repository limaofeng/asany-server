package cn.asany.drive.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.jfantasy.framework.dao.BaseBusEntity;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CLOUD_DRIVE_QUOTA")
public class CloudDriveQuota extends BaseBusEntity {
  @Id
  @Column(name = "DRIVE_ID", nullable = false, updatable = false, precision = 22)
  @GenericGenerator(
      name = "CloudDriveQuotaPkGenerator",
      strategy = "foreign",
      parameters = {@Parameter(name = "property", value = "cloudDrive")})
  @GeneratedValue(generator = "CloudDriveQuotaPkGenerator")
  private Long id;

  /** 文件数 */
  @Column(name = "`COUNT`")
  private Integer count;

  /** 已使用 */
  @Column(name = "`USAGE`")
  private Long usage;

  /** 总大小 */
  @Column(name = "SIZE")
  private Long size;

  /** 云盘 */
  @OneToOne(fetch = FetchType.LAZY, targetEntity = CloudDrive.class, mappedBy = "quota")
  @ToString.Exclude
  private CloudDrive cloudDrive;
}
