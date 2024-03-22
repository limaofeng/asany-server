package cn.asany.pim.core.domain;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Entity
@Table(
    name = "PIM_ASSET",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"SN"},
          name = "UK_PIM_ASSET_SN")
    })
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ASSET_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Asset extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 资产名称 */
  @Column(name = "NAME", length = 100)
  private String name;
  /** 资产类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TYPE_ID", foreignKey = @ForeignKey(name = "FK_PIM_ASSET_TYPE"))
  private AssetType type;
  /** 资产的当前状态，如在用、维修中、已报废等。 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "STATUS_ID", foreignKey = @ForeignKey(name = "FK_PIM_ASSET_STATUS"))
  private AssetStatus status;
}
