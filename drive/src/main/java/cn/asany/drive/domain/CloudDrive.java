package cn.asany.drive.domain;

import cn.asany.base.common.Ownership;
import cn.asany.drive.domain.enums.CloudDriveType;
import cn.asany.organization.core.domain.Organization;
import cn.asany.organization.employee.domain.Employee;
import cn.asany.security.core.domain.User;
import cn.asany.storage.data.domain.Space;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Any;
//import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.mapping.Any.MetaValue;
import org.hibernate.annotations.GenericGenerator;
//import org.hibernate.annotations.MetaValue;
import org.hibernate.mapping.Any.MetaValue;
import org.jfantasy.framework.dao.BaseBusEntity;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "CLOUD_DRIVE",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_STORAGE_CLOUD_DRIVE",
            columnNames = {"OWNER_TYPE", "OWNER", "TYPE"}))
public class CloudDrive extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 云盘类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20)
  private CloudDriveType type;

  /** 存储位置 */
  @ManyToOne
  @JoinColumn(
      name = "SPACE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_CLOUD_DRIVE_SPACE"))
  private Space space;

  /** 存储空间 */
  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "DRIVE_ID")
  @ToString.Exclude
  private CloudDriveQuota quota;

  /** 所有者类型 */
  @Column(name = "OWNER_TYPE", length = 25, nullable = false)
  private String ownerType;

  /** 所有者ID */
  @Column(name = "OWNER", nullable = false)
  private Long ownerId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    CloudDrive that = (CloudDrive) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
