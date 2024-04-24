package cn.asany.organization.employee.domain;

import cn.asany.base.common.domain.Email;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * @author limaofeng
 * @version V1.0 @Description: 员工
 * @date 2022/7/28 9:12 9:12
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
  @TableGenerator
  private Long id;

  @Column(name = "IS_PRIMARY", nullable = false)
  private Boolean primary;

  @Column(name = "LABEL", length = 30)
  private String label;

  @Embedded private Email email;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "EMPLOYEE_ID",
      foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_EMAIL_EID"),
      nullable = false,
      updatable = false)
  private Employee employee;
}
