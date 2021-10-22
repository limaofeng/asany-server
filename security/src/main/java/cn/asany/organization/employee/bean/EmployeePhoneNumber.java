package cn.asany.organization.employee.bean;

import cn.asany.base.common.bean.Phone;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 员工手机
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-03-11 14:19
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_EMPLOYEE_PHONE_NUMBER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employeePositions", "links"})
public class EmployeePhoneNumber extends BaseBusEntity {

  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 主号码 */
  @Column(name = "IS_PRIMARY", nullable = false)
  private Boolean primary;
  /** 标签 */
  @Column(name = "LABEL", length = 30)
  private String label;
  /** 电话 */
  @Embedded private Phone phone;
  /** 员工 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "EMPLOYEE_ID",
      foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_PHONE_EID"),
      nullable = false,
      updatable = false)
  private Employee employee;
}
