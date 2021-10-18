package cn.asany.organization.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 组织状态
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019/11/13 11:36 上午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORG_EMPLOYEE_STATUS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeStatus extends BaseBusEntity {
  /** ID */
  @Id
  @Column(name = "ID", length = 20)
  @GeneratedValue(generator = "org_organization_employee_status_gen")
  @TableGenerator(
      name = "org_organization_employee_status_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "org_organization_employee_status:id",
      valueColumnName = "gen_value")
  private Long id;
  /** 编码 */
  @Column(name = "CODE", length = 255)
  private String code;
  /** 名称 */
  @Column(name = "NAME", length = 255)
  private String name;
  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_EMPLOYEE_STATUS_ORGANIZATION"),
      updatable = false,
      nullable = false)
  private Organization organization;
  /** 所属组织纬度 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DIMENSION_ID",
      foreignKey = @ForeignKey(name = "FK_EMPLOYEE_STATUS_DIMENSION"),
      updatable = false,
      nullable = false)
  private OrganizationDimension dimension;
  /** 是否是默认值 */
  @Column(name = "is_default", length = 10)
  private Boolean isDefault;
}
