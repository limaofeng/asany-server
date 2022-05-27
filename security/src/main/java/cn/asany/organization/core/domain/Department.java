package cn.asany.organization.core.domain;

import cn.asany.organization.core.domain.databind.DepartmentDeserializer;
import cn.asany.organization.core.domain.databind.DepartmentSerializer;
import cn.asany.organization.core.domain.databind.OrganizationSerializer;
import cn.asany.organization.relationship.domain.EmployeePosition;
import cn.asany.organization.relationship.domain.Position;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.security.Permission;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 部门
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-03-11 14:35
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "ORG_DEPARTMENT",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_DEPARTMENT_CODE",
            columnNames = {"ORGANIZATION_ID", "DIMENSION_ID", "CODE"}))
@JsonIgnoreProperties({
  "hibernateLazyInitializer",
  "handler",
  "children",
  "employees",
  "positions",
  "roles",
  "links",
  "grants"
})
public class Department extends BaseBusEntity {

  public static final String OWNERSHIP_KEY = "DEPARTMENT";

  public static final String PATH_SEPARATOR = "/";

  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 简写 */
  @Column(name = "CODE", length = 10)
  private String code;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 路径 */
  @Column(name = "PATH", length = 50)
  private String path;
  /** 排序字段 */
  @Column(name = "SORT")
  private Integer index;
  /** 层级 */
  @Column(name = "LEVEL")
  private Integer level;
  /** 描述信息 */
  @Column(name = "DESCRIPTION", length = 150)
  private String description;
  /** 是否启用 0禁用 1 启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
  /** 部门类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TYPE", foreignKey = @ForeignKey(name = "FK_ORG_DEPARTMENT_TID"))
  private DepartmentType type;
  /** 分组人数统计 */
  @Column(name = "COUNT")
  private Long count;
  /** 上级部门 */
  @JsonSerialize(using = DepartmentSerializer.class)
  @JsonDeserialize(using = DepartmentDeserializer.class)
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_AUTH_DEPARTMENT_PID"))
  private Department parent;
  /** 下属部门 */
  @JsonInclude(content = JsonInclude.Include.NON_NULL)
  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @OrderBy("sort ASC")
  private List<Department> children;
  /** 本部门成员 */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.REMOVE)
  private List<EmployeePosition> employees;
  /** 对应的岗位 */
  @OneToMany(
      mappedBy = "department",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<Position> positions;
  /** 组织机构 */
  @JsonSerialize(using = OrganizationSerializer.class)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_DEPARTMENT_ORGANIZATION"),
      updatable = false,
      nullable = false)
  private Organization organization;
  /** 组织纬度 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DIMENSION_ID",
      foreignKey = @ForeignKey(name = "FK_DEPARTMENT_ORGANIZATION_DIMENSION"),
      updatable = false,
      nullable = false)
  private OrganizationDimension dimension;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.REMOVE)
  private List<DepartmentAttribute> attributes;

  /** 部门权限 */
  @Transient private List<Permission> permissions;

  /** 签名图与部门关联 */
  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @JoinTable(
      name = "ORG_DEPARTMENT_AUTOGRAPH_PNG_ITEM",
      joinColumns =
          @JoinColumn(
              name = "DEPARTMENT_ID",
              foreignKey = @ForeignKey(name = "FK_ORG_DEPARTMENT_AUTOGRAPH_PNG_ITEM_DEPARTMENT")),
      inverseJoinColumns = @JoinColumn(name = "AUTOGRAPH_ID"),
      foreignKey = @ForeignKey(name = "FK_ORG_DEPARTMENT_AUTOGRAPH_PNG_ITEM_AUTOGRAPH"))
  public Long getParentId() {
    return this.parent != null ? this.parent.getId() : null;
  }

  public Long getOrganizationId() {
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
        .append(code, that.code)
        .append(name, that.name)
        .append(index, that.index)
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
        .append(code)
        .append(name)
        .append(index)
        .append(description)
        .append(getParentId())
        .append(getOrganizationId())
        .toHashCode();
  }
}
