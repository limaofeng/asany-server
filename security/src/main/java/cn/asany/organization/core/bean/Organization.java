package cn.asany.organization.core.bean;

import cn.asany.base.common.Ownership;
import cn.asany.security.core.bean.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 组织机构
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-22 下午04:00:57
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "ORG_ORGANIZATION",
    uniqueConstraints = @UniqueConstraint(columnNames = "CODE", name = "UK_ORGANIZATION_CODE"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "children", "employees"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Organization extends BaseBusEntity implements Ownership {

  public static final String DEFAULT_DIMENSION = "members";
  public static final String OWNERSHIP_KEY = "ORGANIZATION";

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 编码 */
  @Column(name = "CODE", length = 10)
  private String code;
  /** 机构名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 排序字段 */
  @Column(name = "SORT")
  private Integer sort;
  /** 机构描述信息 */
  @Column(name = "DESCRIPTION", length = 150)
  private String description;
  /** 备注 */
  @Column(name = "remark", length = 300)
  private String remark;
  /** 所有者 - 用户ID */
  @JoinColumn(name = "OWNERSHIP_ID", insertable = false, updatable = false)
  private User ownership;
  /** 部门类型 */
  @JsonManagedReference
  @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<DepartmentType> departmentTypes;
  /** 组织纬度 */
  @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<OrganizationDimension> dimensions;

  @Override
  @Transient
  public String getOwnerType() {
    return OWNERSHIP_KEY;
  }
}
