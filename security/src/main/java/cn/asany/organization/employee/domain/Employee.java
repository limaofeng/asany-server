package cn.asany.organization.employee.domain;

import cn.asany.base.common.Ownership;
import cn.asany.base.common.domain.Email;
import cn.asany.base.common.domain.Phone;
import cn.asany.organization.core.domain.EmployeeGroup;
import cn.asany.organization.core.domain.EmployeeIdentity;
import cn.asany.organization.core.domain.Organization;
import cn.asany.organization.employee.domain.enums.InviteStatus;
import cn.asany.organization.relationship.domain.EmployeePosition;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.domain.enums.Sex;
import cn.asany.storage.api.FileObject;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringArrayConverter;

/**
 * 员工
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
@Table(
    name = "ORG_EMPLOYEE",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"ORGANIZATION_ID", "JOB_NUMBER"},
          name = "UK_EMPLOYEE_JOB_NUMBER")
    })
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employeePositions", "links", "user"})
public class Employee extends BaseBusEntity implements Ownership {

  public static final String OWNERSHIP_KEY = "EMPLOYEE";

  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 头像 */
  @Type(type = "file")
  @Column(name = "avatar", length = 500)
  private FileObject avatar;

  /** 工号 */
  @Column(name = "JOB_NUMBER", precision = 20)
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

  /** 邀请状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "INVITE_STATUS", length = 10)
  private InviteStatus inviteStatus;

  /** 组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_EMPLOYEE_ORGANIZATION"),
      updatable = false,
      nullable = false)
  private Organization organization;

  /** 组织内的身份 */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.REMOVE)
  private List<EmployeeIdentity> identities;

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
  private List<EmployeePosition> positions;

  /** 链接到的账户 */
  @OneToMany(
      mappedBy = "employee",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<EmployeeLink> links;

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
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_ORG_EMPLOYEE_USER"))
  private User user;

  public static EmployeeBuilder builder() {
    return new EmployeeBuilder();
  }

  public static EmployeeBuilder builder(User user) {
    EmployeeBuilder builder = Employee.builder();
    builder.avatar(user.getAvatar());
    builder.name(user.getName());
    builder.user(user);
    return builder;
  }

  @Override
  public String getOwnerType() {
    return OWNERSHIP_KEY;
  }

  public static class EmployeeBuilder {

    public EmployeeBuilder addEmail(String label, String emailAddress) {
      if (this.emails == null) {
        this.emails = new ArrayList<>();
      }
      this.emails.add(
          EmployeeEmail.builder()
              .label(label)
              .email(Email.builder().address(emailAddress).build())
              .primary(this.emails.isEmpty())
              .build());
      return this;
    }

    public EmployeeBuilder addPhone(String label, String phoneNumber) {
      if (this.phones == null) {
        this.phones = new ArrayList<>();
      }
      this.phones.add(
          EmployeePhoneNumber.builder()
              .label(label)
              .phone(Phone.builder().number(phoneNumber).build())
              .primary(this.phones.isEmpty())
              .build());
      return this;
    }
  }
}
