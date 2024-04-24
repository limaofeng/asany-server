package cn.asany.pim.core.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@Entity
@Table(
    name = "PIM_ASSET",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"PRODUCT_ID", "SN"},
          name = "UK_PIM_ASSET_SN")
    })
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ASSET_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Asset extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  /** 资产编号 */
  @Column(name = "NO", length = 50)
  private String no;

  /** 资产名称 */
  @Column(name = "NAME", length = 100)
  private String name;

  /** 资产类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ASSET_TYPE",
      foreignKey = @ForeignKey(name = "FK_PIM_ASSET_TYPE"),
      insertable = false,
      updatable = false)
  private AssetType type;

  /** 资产的当前状态，如在用1、维修中、已报废等。 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "STATUS_ID", foreignKey = @ForeignKey(name = "FK_PIM_ASSET_STATUS"))
  private AssetStatus status;
}
