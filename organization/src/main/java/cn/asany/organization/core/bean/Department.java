package cn.asany.organization.core.bean;

import cn.asany.organization.core.bean.databind.OrganizationSerializer;
import cn.asany.organization.core.bean.databind.DepartmentDeserializer;
import cn.asany.organization.core.bean.databind.DepartmentSerializer;
import cn.asany.organization.core.bean.enums.LinkType;
import cn.asany.organization.relationship.bean.EmployeePosition;
import cn.asany.organization.relationship.bean.OrganizationEmployee;
import cn.asany.organization.relationship.bean.Position;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.security.Permission;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: 部门
 * @date 2019-03-11 14:35
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_DEPARTMENT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "children", "employees", "positions", "roles", "links", "grants"})
public class Department extends BaseBusEntity {

    @Id
    @Column(name = "ID", precision = 22)
    @GeneratedValue(generator = "org_department_gen")
    @TableGenerator(name = "org_department_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "org_department:id", valueColumnName = "gen_value")
    private Long id;
    /**
     * 简写
     */
    @Column(name = "sn", length = 10)
    private String sn;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 50)
    private String name;
    /**
     * 路径
     */
    @Column(name = "PATH", length = 50)
    private String path;
    /**
     * 排序字段
     */
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 描述信息
     */
    @Column(name = "DESCRIPTION", length = 150)
    private String description;
    /**
     * 是否启用 0禁用 1 启用
     */
    @Column(name = "ENABLED")
    private Boolean enabled;
    /**
     * 部门类型
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE", foreignKey = @ForeignKey(name = "FK_ORG_DEPARTMENT_TID"))
    private DepartmentType type;
    /**
     * 上级部门
     */
    @JsonSerialize(using = DepartmentSerializer.class)
    @JsonDeserialize(using = DepartmentDeserializer.class)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_AUTH_DEPARTMENT_PID"))
    private Department parent;
    /**
     * 下属部门
     */
    @JsonInclude(content = JsonInclude.Include.NON_NULL)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("sort ASC")
    private List<Department> children;
    /**
     * 本部门成员
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.REMOVE)
    private List<EmployeePosition> employees;
    /**
     * 对应的岗位
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Position> positions;
    /**
     * 部门拥有的角色
     */
//    @JsonIgnore
//    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
//    @JoinTable(name = "AUTH_ROLE_DEPARTMENT", joinColumns = @JoinColumn(name = "DEPARTMENT_ID", foreignKey = @ForeignKey(name = "FK_AUTH_ROLE_DEPARTMENT_DID")), inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"), foreignKey = @ForeignKey(name = "FK_ROLE_DEPARTMENT_RID"))
//    private List<Role> roles;
    /**
     * 组织机构
     */
    @JsonSerialize(using = OrganizationSerializer.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey(name = "FK_DEPARTMENT_ORGANIZATION"), updatable = false, nullable = false)
    private Organization organization;
    /**
     * 链接到的账户
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<DepartmentLink> links;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.REMOVE)
    private List<DepartmentAttribute> attributes;


    /**
     * 部门权限
     */
    @Transient
    private List<Permission> permissions;

    @OneToMany(mappedBy = "department",fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<OrganizationEmployee> organizationEmployees;

    /**
     * 签名图与部门关联
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinTable(name = "ORG_DEPARTMENT_AUTOGRAPH_PNG_ITEM",
        joinColumns = @JoinColumn(name = "DEPARTMENT_ID", foreignKey = @ForeignKey(name = "FK_ORG_DEPARTMENT_AUTOGRAPH_PNG_ITEM_DEPARTMENT")),
        inverseJoinColumns = @JoinColumn(name = "AUTOGRAPH_ID"), foreignKey = @ForeignKey(name = "FK_ORG_DEPARTMENT_AUTOGRAPH_PNG_ITEM_AUTOGRAPH")
    )

    public Long getParentId() {
        return this.parent != null ? this.parent.getId() : null;
    }

    public String getOrganizationId() {
        return this.organization != null ? this.organization.getId() : null;
    }

    public void setParentId(Long parentId) {
        this.parent = parentId == null ? null : Department.builder().id(parentId).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Department that = (Department) o;

        return new EqualsBuilder()
            .appendSuper(super.equals(o))
            .append(id, that.id)
            .append(sn, that.sn)
            .append(name, that.name)
            .append(sort, that.sort)
            .append(description, that.description)
            .append(getParentId(), that.getParentId())
            .append(getOrganizationId(), that.getOrganizationId())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .appendSuper(super.hashCode())
            .append(id)
            .append(sn)
            .append(name)
            .append(sort)
            .append(description)
            .append(getParentId())
            .append(getOrganizationId())
            .toHashCode();
    }

    public String getLinkId(LinkType idType) {
        Optional<DepartmentLink> link = this.getLinks().stream().filter(item -> item.getType().name().equals(idType.name())).findAny();
        if (!link.isPresent()) {
            return null;
        }
        return link.get().getLinkId();
    }

    @JsonIgnore
    public Set<String> getAuthoritys() {
        Set<String> authoritys = new HashSet<>();
//        for (String id : StringUtil.tokenizeToStringArray(this.getPath(), "/")) {
//            authoritys.add(SecurityScope.newInstance(SecurityType.department, id).toString());
//        }
        return authoritys;
    }
}
