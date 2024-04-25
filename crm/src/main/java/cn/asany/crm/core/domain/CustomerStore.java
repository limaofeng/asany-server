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
package cn.asany.crm.core.domain;

import cn.asany.base.common.Ownership;
import cn.asany.base.common.domain.Address;
import cn.asany.base.common.domain.ContactInformation;
import cn.asany.base.common.domain.Geolocation;
import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 客户门店
 *
 * @author limaofeng
 */
@Data
@Entity
@Builder
@Table(name = "CRM_CUSTOMER_STORE")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomerStore extends BaseBusEntity implements Ownership {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  /** 门店编号 */
  @Column(name = "NO", length = 50)
  private String no;

  /** 门店名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 开业时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "OPENING_DATE")
  private Date openingDate;

  /** 门店电话 */
  @Column(name = "PHONE", length = 20)
  private String phone;

  /** 门店地址 */
  @Embedded private Address address;

  /** 门店位置 (经纬坐标) */
  @Embedded private Geolocation location;

  /** 门店联系方式 */
  @Embedded private ContactInformation contactInfo;

  /** 客户 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CUSTOMER_ID", foreignKey = @ForeignKey(name = "FK_CUSTOMER_STORE_CUSTOMER"))
  private Customer customer;
}
