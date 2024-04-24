package cn.asany.organization.core.domain;

import cn.asany.organization.employee.domain.Employee;
import cn.asany.organization.relationship.domain.Position;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 员工角色
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "ORG_EMPLOYEE_IDENTITY",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"ORGANIZATION_ID", "DIMENSION_ID", "EMPLOYEE_ID"},
          name = "UK_EMPLOYEE_IDENTITY_UNIQUE")
    })
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeIdentity extends BaseBusEntity {
  /** ID */
  @Id
  @Column(name = "ID", length = 20)
  @TableGenerator
  private Long id;

  /** 组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_DIMENSION_MEMBER_ORG"),
      updatable = false,
      nullable = false)
  private Organization organization;

  /** 纬度 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DIMENSION_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_DIMENSION_MEMBER_DIMENSION"),
      updatable = false,
      nullable = false)
  private OrganizationDimension dimension;

  /** 成员 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "EMPLOYEE_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_DIMENSION_MEMBER_EMPLOYEE"),
      nullable = false,
      updatable = false)
  private Employee employee;

  /** 状态 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "STATUS_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_DIMENSION_MEMBER_STATUS"),
      nullable = false)
  private EmployeeStatus status;

  /** 部门 / 群组 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DEPARTMENT_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_DIMENSION_MEMBER_DEPARTMENT"))
  private Department department;

  /** 职务 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "POSITION_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_DIMENSION_MEMBER_POSITION"))
  private Position position;
}
