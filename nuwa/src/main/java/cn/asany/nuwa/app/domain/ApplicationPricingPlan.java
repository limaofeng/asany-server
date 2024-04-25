/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.nuwa.app.domain;

import cn.asany.nuwa.app.domain.enums.PaymentCycle;
import cn.asany.nuwa.app.domain.enums.PlanStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 定价方案
 *
 * @author limaofeng
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NUWA_APPLICATION_PRICING_PLAN")
public class ApplicationPricingPlan extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @TableGenerator
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    ApplicationPricingPlan that = (ApplicationPricingPlan) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
