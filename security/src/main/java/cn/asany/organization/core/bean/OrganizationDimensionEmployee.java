package cn.asany.organization.core.bean;

import cn.asany.organization.employee.bean.Employee;
import cn.asany.organization.relationship.bean.Position;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
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
@Table(name = "ORG_ORGANIZATION_DIMENSION_MEMBER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrganizationDimensionEmployee extends BaseBusEntity {
  /** ID */
  @Id
  @Column(name = "ID", length = 20)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
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
  /** 主部门 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DEPARTMENT_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_DIMENSION_MEMBER_DEPARTMENT"),
      nullable = false)
  private Department department;
  /** 职务 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "POSITION_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_DIMENSION_MEMBER_POSITION"),
      nullable = false)
  private Position position;
}
