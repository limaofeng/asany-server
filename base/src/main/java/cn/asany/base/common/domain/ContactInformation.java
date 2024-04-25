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
package cn.asany.base.common.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 联系方式 */
@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformation {
  /** 联系人姓名 */
  @Column(name = "CONTACT_NAME", length = 50)
  private String name;

  /** 邮箱 */
  @Column(name = "CONTACT_EMAIL", length = 50)
  private String email;

  /** 电话 */
  @Column(name = "CONTACT_PHONE", length = 20)
  private String phone;

  /** 地址 */
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "country", column = @Column(name = "CONTACT_COUNTRY", length = 30)),
    @AttributeOverride(name = "province", column = @Column(name = "CONTACT_PROVINCE", length = 30)),
    @AttributeOverride(name = "city", column = @Column(name = "CONTACT_CITY", length = 30)),
    @AttributeOverride(name = "district", column = @Column(name = "CONTACT_DISTRICT", length = 30)),
    @AttributeOverride(name = "street", column = @Column(name = "CONTACT_STREET", length = 30)),
    @AttributeOverride(
        name = "detailedAddress",
        column = @Column(name = "CONTACT_DETAILED_ADDRESS", length = 100)),
    @AttributeOverride(
        name = "postalCode",
        column = @Column(name = "CONTACT_POSTAL_CODE", length = 10))
  })
  Address address;
}
