package cn.asany.organization.relationship.bean;

import cn.asany.organization.core.bean.Department;
import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.bean.OrganizationEmployeeStatus;
import cn.asany.organization.employee.bean.Employee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 组织管理
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019/11/13 11:35 上午
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORG_ORGANIZATION_EMPLOYEE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrganizationEmployee extends BaseBusEntity {
  /** ID */
  @Id
  @Column(name = "ID", length = 20)
  @GeneratedValue(generator = "org_organization_employee_gen")
  @TableGenerator(
      name = "org_organization_employee_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "org_organization_employee:id",
      valueColumnName = "gen_value")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_EMPLOYEE_OID"),
      updatable = false,
      nullable = false)
  private Organization organization;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "EMPLOYEE_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_EMPLOYEE_EID"),
      nullable = false,
      updatable = false)
  private Employee employee;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "STATUS_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_EMPLOYEE_SID"),
      nullable = false)
  private OrganizationEmployeeStatus status;

  /** 主部门 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DEPARTMENT_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_EMPLOYEE_DEPARTMENT_ID"),
      nullable = false)
  private Department department;

  /** 主职务 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "POSITION_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_EMPLOYEE_POSITION_ID"),
      nullable = false)
  private Position position;

  /** 岗位 */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "status", cascade = CascadeType.REMOVE)
  private List<EmployeePosition> positions;
}
