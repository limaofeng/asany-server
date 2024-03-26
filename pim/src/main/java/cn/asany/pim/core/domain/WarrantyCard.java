package cn.asany.pim.core.domain;

import cn.asany.pim.core.domain.enums.WarrantyStatus;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Entity
@Table(name = "PIM_WARRANTY_CARD")
@EqualsAndHashCode(callSuper = true)
public class WarrantyCard extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 保修卡名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 保修开始日期 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "START_DATE")
  private Date startDate;
  /** 保修结束日期 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "END_DATE")
  private Date endDate;
  /** 保修卡状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20, nullable = false)
  private WarrantyStatus status;
  /** 设备 */
  @ManyToOne
  @JoinColumn(name = "DEVICE_ID", foreignKey = @ForeignKey(name = "FK_WARRANTY_CARD_DEVICE_ID"))
  private Device device;
  /** 保修政策 */
  @ManyToOne
  @JoinColumn(
      name = "WARRANTY_POLICY_ID",
      foreignKey = @ForeignKey(name = "FK_WARRANTY_CARD_WARRANTY_POLICY_ID"))
  private WarrantyPolicy policy;
}
