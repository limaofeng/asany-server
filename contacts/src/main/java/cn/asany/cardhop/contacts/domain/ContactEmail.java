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
package cn.asany.cardhop.contacts.domain;

import cn.asany.base.common.domain.Email;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CARDHOP_CONTACT_EMAIL")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "contact"})
public class ContactEmail extends BaseBusEntity {

  @Id
  @Column(name = "ID", precision = 22)
  @TableGenerator
  private Long id;

  @Column(name = "IS_PRIMARY", nullable = false)
  private Boolean primary;

  @Column(name = "LABEL", length = 30)
  private String label;

  @Embedded private Email email;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "CONTACT_ID",
      foreignKey = @ForeignKey(name = "FK_CARDHOP_CONTACT_EMAIL_CID"),
      nullable = false,
      updatable = false)
  private Contact contact;
}
