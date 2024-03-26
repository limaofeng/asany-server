package cn.asany.pim.core.domain;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 状态编码 */
  @Column(name = "CODE", length = 20, nullable = false)
  private String code;
  /** 状态名称 */
  @Column(name = "NAME", length = 20, nullable = false)
  private String name;
}
