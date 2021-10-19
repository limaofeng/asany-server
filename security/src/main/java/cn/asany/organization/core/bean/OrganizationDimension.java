package cn.asany.organization.core.bean;

import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 组织纬度
 *
 * @author limaofeng
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
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
}
