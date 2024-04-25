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

import cn.asany.cardhop.contacts.domain.enums.ContactBookType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 通信录(Address Book)
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2022-3-15 上午11:53:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "ContactBook")
@Table(name = "CARDHOP_CONTACT_BOOK")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class ContactBook extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  @Column(name = "NAME", length = 20)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private ContactBookType type;

  /** 所有者 */
  //  @Any(
  //      metaColumn = @Column(name = "OWNER_TYPE", length = 20, insertable = false, updatable =
  // false),
  //      fetch = FetchType.LAZY)
  //  @AnyMetaDef(
  //      idType = "long",
  //      metaType = "string",
  //      metaValues = {
  //        @MetaValue(targetEntity = User.class, value = User.OWNERSHIP_KEY),
  //        @MetaValue(targetEntity = Organization.class, value = Organization.OWNERSHIP_KEY)
  //      })
  //  @JoinColumn(name = "OWNER", insertable = false, updatable = false)
  //  private Ownership owner;

  /** 所有联系人 */
  @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
  @OrderBy("createdAt DESC")
  private List<Contact> contacts;

  /** 联系人分组列表 */
  @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
  @OrderBy("createdAt ASC")
  private List<ContactGroup> groups;
}
