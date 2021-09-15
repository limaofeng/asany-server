package cn.asany.organization.employee.bean;

import cn.asany.organization.core.bean.Department;
import cn.asany.organization.core.bean.EmployeeGroup;
import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.bean.OrganizationEmployeeStatus;
import cn.asany.organization.employee.bean.enums.Sex;
import cn.asany.organization.relationship.bean.EmployeePosition;
import cn.asany.organization.relationship.bean.Position;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringArrayConverter;

/**
 * 员工
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
@Table(name = "ORG_EMPLOYEE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employeePositions", "links", "user"})
public class Employee extends BaseBusEntity {
  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "org_employee_gen")
  @TableGenerator(
      name = "org_employee_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "org_employee:id",
      valueColumnName = "gen_value")
  private Long id;
  /** 头像 */
  @Convert(converter = FileObjectConverter.class)
  @Column(name = "avatar", precision = 500)
  private FileObject avatar;
  /** 工号 */
  @Column(name = "SN", nullable = false, precision = 20)
  private String jobNumber;
  /** 名称 */
  @Column(name = "NAME", length = 30)
  private String name;
  /** 生日 */
  @Column(name = "BIRTHDAY")
  @Temporal(TemporalType.DATE)
  private Date birthday;
  /** 性别 */
  @Enumerated(EnumType.STRING)
  @Column(name = "SEX", length = 10)
  private Sex sex;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "STATUS", foreignKey = @ForeignKey(name = "FK_EMPLOYEE_SID"), nullable = false)
  private OrganizationEmployeeStatus status;

  /** 部门 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DEPARTMENT_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_EMPLOYEE_DEPARTMENT_ID"),
      nullable = false)
  private Department department;

  /** 职务 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "POSITION_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_EMPLOYEE_POSITION_ID"),
      nullable = false)
  private Position position;

  /** 地址列表 */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.REMOVE)
  private List<EmployeeAddress> addresses;

  /** 邮箱列表 */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.REMOVE)
  private List<EmployeeEmail> emails;

  /** 电话列表 */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.REMOVE)
  private List<EmployeePhoneNumber> phones;

  /** 部门与岗位 */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.REMOVE)
  private List<EmployeePosition> employeePositions;

  /** 链接到的账户 */
  @OneToMany(
      mappedBy = "employee",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<EmployeeLink> links;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_EMPLOYEE_OID"),
      updatable = false,
      nullable = false)
  private Organization organization;

  /** 用户标签，用于筛选用户 */
  @Column(name = "TAGS", precision = 300)
  @Convert(converter = StringArrayConverter.class)
  private String[] tags;

  /** 群组 */
  @ManyToMany(
      targetEntity = EmployeeGroup.class,
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @JoinTable(
      name = "ORG_EMPLOYEE_GROUP_EMPLOYEE",
      joinColumns = @JoinColumn(name = "EMPLOYEE_ID"),
      inverseJoinColumns = @JoinColumn(name = "GROUP_ID"),
      foreignKey = @ForeignKey(name = "FK_EMPLOYEE_GROUP_EID"))
  private List<EmployeeGroup> groups;

  @OneToMany(
      mappedBy = "stargazer",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<Star> stars;

  /** 关联用户 */
  @Column(name = "USER_ID", precision = 22)
  private Long user;
}
