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
import cn.asany.base.common.domain.ContactInformation;
import cn.asany.crm.core.domain.enums.CustomerTicketStrategy;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 客户
 *
 * @author limaofeng
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CRM_CUSTOMER")
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Customer extends BaseBusEntity implements Ownership {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  /** 客户名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 联系方式 */
  @Embedded private ContactInformation contactInfo;

  /** 客户门店 */
  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.REMOVE)
  private List<CustomerStore> stores;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "TICKET_STRATEGY", length = 10)
  private CustomerTicketStrategy ticketStrategy = CustomerTicketStrategy.AUTO;
}
