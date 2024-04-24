package cn.asany.organization.core.domain;

import cn.asany.organization.employee.domain.Employee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/** 用户组 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_EMPLOYEE_GROUP")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeGroup extends BaseBusEntity {

  private static final long serialVersionUID = 7898475330929818969L;

  @Id
  @Column(name = "ID", nullable = false)
  @TableGenerator
  private Long id;

  /** 用户组名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  /** 使用范围 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "SCOPE",
      foreignKey = @ForeignKey(name = "FK_EMPLOYEE_GROUP_SCOPEID"),
      updatable = false,
      nullable = false)
  private EmployeeGroupScope scope;

  /** 群组成员 */
  @ManyToMany(targetEntity = Employee.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "ORG_EMPLOYEE_GROUP_EMPLOYEE",
      joinColumns = @JoinColumn(name = "GROUP_ID"),
      inverseJoinColumns = @JoinColumn(name = "EMPLOYEE_ID"),
      foreignKey = @ForeignKey(name = "FK_EMPLOYEE_GROUP_GID"))
  private List<Employee> employees;

  //    /**
  //     * 群组拥有的角色
  //     */
  //    @JsonIgnore
  //    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
  //    @JoinTable(name = "AUTH_ROLE_EMPLOYEE_GROUP",
  //            joinColumns = @JoinColumn(name = "EMPLOYEE_GROUP_ID"),
  //            inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"))
  //    private List<Role> roles;
}
