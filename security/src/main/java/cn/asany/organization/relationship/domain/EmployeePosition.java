package cn.asany.organization.relationship.domain;

import cn.asany.organization.core.domain.Department;
import cn.asany.organization.core.domain.Organization;
import cn.asany.organization.core.domain.OrganizationDimension;
import cn.asany.organization.core.domain.databind.DepartmentDeserializer;
import cn.asany.organization.core.domain.databind.DepartmentSerializer;
import cn.asany.organization.employee.domain.Employee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 员工职位
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_EMPLOYEE_POSITION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeePosition extends BaseBusEntity {

  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 是否为主部门 */
  @Column(name = "IS_PRIMARY")
  private Boolean primary;
  /** 员工 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "EMPLOYEE_ID",
      foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_POSITION_EID"),
      nullable = false,
      updatable = false)
  private Employee employee;
  /** 员工所在部门的职位 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "POSITION_ID", foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_POSITION_PID"))
  private Position position;
  /** 所在部门 */
  @JsonSerialize(using = DepartmentSerializer.class)
  @JsonDeserialize(using = DepartmentDeserializer.class)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DEPARTMENT_ID",
      foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_POSITION_DID"),
      nullable = false)
  private Department department;
  /** 纬度 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DIMENSION_ID",
      foreignKey = @ForeignKey(name = "FK_EMPLOYEE_POSITION_DIMENSION"),
      updatable = false,
      nullable = false)
  private OrganizationDimension dimension;
  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_EMPLOYEE_POSITION_OID"),
      updatable = false,
      nullable = false)
  private Organization organization;
}
