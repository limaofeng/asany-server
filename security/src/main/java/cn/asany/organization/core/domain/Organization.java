package cn.asany.organization.core.domain;

import cn.asany.base.common.Ownership;
import cn.asany.base.common.domain.Address;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
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
  public static final String DEFAULT_ORGANIZATION_CODE = "asany";

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
  /** 组织 */
  @Convert(converter = FileObjectConverter.class)
  @Column(name = "LOGO", precision = 500)
  private FileObject logo;
  /** 排序字段 */
  @Column(name = "SORT")
  private Integer sort;
  /** 机构描述信息 */
  @Column(name = "DESCRIPTION", length = 150)
  private String description;
  /** 邮箱 */
  @Column(name = "EMAIL", length = 25)
  private String email;
  /** 网址 */
  @Column(name = "URL", length = 100)
  private String url;
  /** 位置 */
  @Embedded private Address location;
  /** 部门类型 */
  @JsonManagedReference
  @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<DepartmentType> departmentTypes;
  /** 组织纬度 */
  @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<OrganizationDimension> dimensions;
  /** 组织成员 */
  @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<OrganizationMember> members;

  @Override
  @Transient
  public String getOwnerType() {
    return OWNERSHIP_KEY;
  }
}
