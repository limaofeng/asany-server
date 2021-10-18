package cn.asany.organization.core.bean;

import cn.asany.base.common.Ownership;
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
  private Long ownership;

  /** 部门类型 */
  @JsonManagedReference
  @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<DepartmentType> departmentTypes;
  /** 用户组范围 */
  @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<EmployeeGroupScope> employeeGroupScopes;

  /** 是否支持多部门 */
  @Column(name = "MULTI_SECTORAL")
  private Boolean multiSectoral;

  /** 一个人最多可以加入到几个部门中 */
  @Column(name = "MULTI_SECTORAL_NUMBER")
  private Long multiSectoralNumber;

  @Override
  @Transient
  public String getOwnerType() {
    return OWNERSHIP_KEY;
  }
}
