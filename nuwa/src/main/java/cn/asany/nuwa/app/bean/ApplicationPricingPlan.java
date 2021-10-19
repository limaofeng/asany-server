package cn.asany.nuwa.app.bean;

import cn.asany.nuwa.app.bean.enums.PaymentCycle;
import cn.asany.nuwa.app.bean.enums.PlanStatus;
import java.math.BigDecimal;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 定价方案
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "NUWA_APPLICATION_PRICING_PLAN")
public class ApplicationPricingPlan extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 100)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 50)
  private PlanStatus status;

  /** 付费周期 */
  @Column(name = "PAYMENT_CYCLE", length = 20)
  private PaymentCycle paymentCycle;

  /** 价格 */
  @Column(name = "PRICE")
  private BigDecimal price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "APPLICATION_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_PRICING_PLAN_APP"))
  private Application application;
}
