package cn.asany.organization.core.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 组织纬度
 *
 * @author limaofeng
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "ORG_ORGANIZATION_DIMENSION",
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"ORGANIZATION_ID", "CODE"},
            name = "UK_ORGANIZATION_DIMENSION_CODE"))
public class OrganizationDimension extends BaseBusEntity {

  public static final String KEY_OF_DEFAULT = "default";

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_DIMENSION_ORG"),
      updatable = false,
      nullable = false)
  private Organization organization;
  /** 编码 */
  @Column(name = "CODE", length = 200, nullable = false)
  private String code;
  /** 纬度名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 排序字段 */
  @Column(name = "SORT")
  private Integer sort;
  /** 描述信息 */
  @Column(name = "DESCRIPTION", length = 150)
  private String description;
  /** 状态 */
  @OneToMany(mappedBy = "dimension", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<EmployeeStatus> statuses;
  /** 部门 */
  @OneToMany(mappedBy = "dimension", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<Department> departments;
  /** 人数统计 */
  @Column(name = "COUNT")
  private Long count;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    OrganizationDimension dimension = (OrganizationDimension) o;
    return id != null && Objects.equals(id, dimension.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
