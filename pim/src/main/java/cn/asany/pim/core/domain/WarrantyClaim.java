package cn.asany.pim.core.domain;

import cn.asany.pim.core.domain.enums.WarrantyClaimStatus;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Entity
@Table(name = "PIM_WARRANTY_CLAIM")
@EqualsAndHashCode(callSuper = true)
public class WarrantyClaim extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
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
