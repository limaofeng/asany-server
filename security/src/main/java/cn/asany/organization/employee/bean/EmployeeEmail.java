package cn.asany.organization.employee.bean;

import cn.asany.base.common.bean.enums.EmailStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng
 * @version V1.0 @Description: 员工
 * @date 2019-03-11 14:19
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_EMPLOYEE_EMAIL")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employee"})
public class EmployeeEmail extends BaseBusEntity {
  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "org_employee_email_gen")
  @TableGenerator(
      name = "org_employee_email_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "org_employee_email:id",
      valueColumnName = "gen_value")
  private Long id;

  @Column(name = "IS_PRIMARY", nullable = false)
  private Boolean primary;

  @Column(name = "LABEL", length = 30)
  private String label;

  /** 状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false, length = 20)
  private EmailStatus status;
  /** 邮箱 */
  @Column(name = "EMAIL", nullable = false, length = 25)
  private String email;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "EMPLOYEE_ID",
      foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_EMAIL_EID"),
      nullable = false,
      updatable = false)
  private Employee employee;
}
