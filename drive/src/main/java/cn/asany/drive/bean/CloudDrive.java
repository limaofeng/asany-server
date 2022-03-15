package cn.asany.drive.bean;

import cn.asany.base.common.Ownership;
import cn.asany.drive.bean.enums.CloudDriveType;
import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.employee.bean.Employee;
import cn.asany.security.core.bean.User;
import cn.asany.storage.data.bean.Space;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.MetaValue;
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
  @JoinColumn(name = "SPACE_ID", nullable = false)
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
  /** 所有者 */
  @Any(
      metaColumn = @Column(name = "OWNER_TYPE", length = 20, insertable = false, updatable = false),
      fetch = FetchType.LAZY)
  @AnyMetaDef(
      idType = "long",
      metaType = "string",
      metaValues = {
        @MetaValue(targetEntity = User.class, value = User.OWNERSHIP_KEY),
        @MetaValue(targetEntity = Employee.class, value = Employee.OWNERSHIP_KEY),
        @MetaValue(targetEntity = Organization.class, value = Organization.OWNERSHIP_KEY)
      })
  @JoinColumn(name = "OWNER", insertable = false, updatable = false)
  private Ownership owner;

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
