package cn.asany.storage.drive.bean;

import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.employee.bean.Employee;
import cn.asany.security.core.bean.User;
import cn.asany.storage.drive.bean.enums.CloudDriveType;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

@Entity
@Table(name = "STORAGE_CLOUD_DRIVE" /*,
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_STORAGE_CLOUD_DRIVE",
            columnNames = {"STORAGE_ID", "PATH"})*/)
public class CloudDrive extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20)
  private CloudDriveType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EMPLOYEE_ID", foreignKey = @ForeignKey(name = "FK_CLOUD_DRIVE_EMPLOYEE"))
  @ToString.Exclude
  private Employee employee;

  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey(name = "FK_CLOUD_DRIVE_ORG"))
  @ToString.Exclude
  private Organization organization;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "USER_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_CLOUD_DRIVE_UID"))
  @ToString.Exclude
  private User user;
}
