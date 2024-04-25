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
package cn.asany.pim.core.domain;

import cn.asany.pim.core.domain.enums.WarrantyStatus;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Data
@Entity
@Table(name = "PIM_WARRANTY_CARD")
@EqualsAndHashCode(callSuper = true)
public class WarrantyCard extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
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
