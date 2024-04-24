package cn.asany.pim.core.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 资产状态
 *
 * @author limaofeng
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "PIM_ASSET_STATUS")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AssetStatus extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  /** 状态编码 */
  @Column(name = "CODE", length = 20, nullable = false)
  private String code;

  /** 状态名称 */
  @Column(name = "NAME", length = 20, nullable = false)
  private String name;
}
