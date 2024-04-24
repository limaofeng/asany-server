package cn.asany.pim.core.domain;

import cn.asany.pim.core.domain.enums.WarrantyClaimStatus;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Data
@Entity
@Table(name = "PIM_WARRANTY_CLAIM")
@EqualsAndHashCode(callSuper = true)
public class WarrantyClaim extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  @Column(name = "CN", length = 50, nullable = false)
  private String cn;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CLAIM_DATE")
  private Date claimDate;

  /** 保修处理状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20, nullable = false)
  private WarrantyClaimStatus status;

  @ManyToOne
  @JoinColumn(
      name = "WARRANTY_CARD_ID",
      foreignKey = @ForeignKey(name = "FK_WARRANTY_CLAIM_WARRANTY_CARD_ID"))
  private WarrantyCard card;
}
