package cn.asany.organization.employee.bean;

import cn.asany.organization.core.bean.Department;
import cn.asany.organization.core.bean.EmployeeGroup;
import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.relationship.bean.OrganizationEmployee;
import cn.asany.organization.core.bean.enums.LinkType;
import cn.asany.organization.employee.bean.enums.Sex;
import cn.asany.organization.relationship.bean.EmployeePosition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringArrayConverter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.storage.FileObject;
import org.jfantasy.storage.converter.FileObjectConverter;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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
    @TableGenerator(name = "org_employee_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "org_employee:id", valueColumnName = "gen_value")
    private Long id;
    /**
     * 状态
     */
    @Transient
    private String status;
    /**
     * 头像
     */
    @Convert(converter = FileObjectConverter.class)
    @Column(name = "avatar", precision = 500)
    private FileObject avatar;
    /**
     * 工号
     */
    @Column(name = "SN", nullable = false, precision = 20)
    private String jobNumber;
    /**
     * 用户标签，用于筛选用户
     */
    @Column(name = "TAGS", precision = 300)
    @Convert(converter = StringArrayConverter.class)
    private String[] tags;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 30)
    private String name;
    /**
     * 生日
     */
    @Column(name = "BIRTHDAY")
    @Temporal(TemporalType.DATE)
    private Date birthday;
    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "SEX", length = 10)
    private Sex sex;
    /**
     * 移动电话
     */
    @Column(name = "MOBILE", length = 20)
    private String mobile;
    /**
     * 固定电话
     */
    @Column(name = "TEL", length = 20)
    private String tel;
    /**
     * E-mail
     */
    @Column(name = "EMAIL", length = 50)
    private String email;
    /**
     * 员工类型
     */
    @Column(name = "EMPLOYEETYPE", length = 50)
    private String employeetype;
    /**
     * 是否双培养
     */
    @Column(name = "FOSTER", length = 10)
    private String foster;
    /**
     * 加入日期
     */
    @Column(name = "JOINDAY")
    @Temporal(TemporalType.DATE)
    private Date joinday;
    /**
     * 是否双培养
     */
    @Column(name = "PARTYMEMBERSORIGIN", length = 50)
    private String partymembersorigin;

    /**
     * 地址列表
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.REMOVE)
    private List<EmployeeAddress> addresses;

    /**
     * 邮箱列表
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.REMOVE)
    private List<EmployeeEmail> emails;

    /**
     * 电话列表
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.REMOVE)
    private List<EmployeePhone> phones;

    /**
     * 部门
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.REMOVE)
    private List<EmployeePosition> employeePositions;

    /**
     * 链接到的账户
     */
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<EmployeeLink> links;

//    /**
//     * 管理用户
//     */
//    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
//    private User user;

    @Transient
    private Organization currentOrganization;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<OrganizationEmployee> organizationEmployees;

    /**
     * 群组
     */
    @ManyToMany(targetEntity = EmployeeGroup.class, fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinTable(name = "ORG_EMPLOYEE_GROUP_EMPLOYEE", joinColumns = @JoinColumn(name = "EMPLOYEE_ID"), inverseJoinColumns = @JoinColumn(name = "GROUP_ID"), foreignKey = @ForeignKey(name = "FK_EMPLOYEE_GROUP_EID"))
    private List<EmployeeGroup> groups;

    @OneToMany(mappedBy = "stargazer", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Star> stars;

    public List<Long> getDepartmentIds() {
        return ObjectUtil.defaultValue(employeePositions, Collections.<EmployeePosition>emptyList()).stream().map(item -> item.getDepartment().getId()).collect(Collectors.toList());
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.employeePositions = departmentIds.stream().map(id -> {
            EmployeePosition position = new EmployeePosition();
            position.setDepartment(Department.builder().id(id).build());
            return position;
        }).collect(Collectors.toList());
    }

    public String getLinkId(LinkType idType) {
        Optional<EmployeeLink> optional = this.getLinks().stream().filter(item -> item.getType().name().equals(idType.name())).findAny();
        return optional.map(EmployeeLink::getLinkId).orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Employee employee = (Employee) o;

        return new EqualsBuilder()
            .appendSuper(super.equals(o))
            .append(id, employee.id)
            .append(status, employee.status)
            .append(jobNumber, employee.jobNumber)
            .append(tags, employee.tags)
            .append(name, employee.name)
            .append(birthday, employee.birthday)
            .append(sex, employee.sex)
            .append(mobile, employee.mobile)
            .append(tel, employee.tel)
            .append(email, employee.email)
            .append(getDepartmentIds(), employee.getDepartmentIds())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .appendSuper(super.hashCode())
            .append(id)
            .append(status)
            .append(jobNumber)
            .append(tags)
            .append(name)
            .append(birthday)
            .append(sex)
            .append(mobile)
            .append(tel)
            .append(email)
            .append(getDepartmentIds())
            .toHashCode();
    }

    @JsonIgnore
    public Set<String> getAuthoritys() {
        Set<String> authoritys = new HashSet<>();
        return authoritys;
    }

}
