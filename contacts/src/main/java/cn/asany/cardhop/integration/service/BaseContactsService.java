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
package cn.asany.cardhop.integration.service;

import cn.asany.cardhop.contacts.domain.Contact;
import cn.asany.cardhop.contacts.domain.ContactBook;
import cn.asany.cardhop.contacts.domain.ContactGroup;
import cn.asany.cardhop.contacts.domain.enums.ContactBookType;
import cn.asany.cardhop.contacts.service.ContactsService;
import cn.asany.cardhop.integration.IContactsService;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 通讯录
 *
 * @author limaofeng
 */
@Service
public class BaseContactsService implements IContactsService {

  private final ContactsService contactsService;

  public BaseContactsService(ContactsService contactsService) {
    this.contactsService = contactsService;
  }

  @Override
  public ContactBookType getType() {
    return ContactBookType.CARDHOP;
  }

  @Override
  public List<ContactGroup> getGroups(ContactBook book, String namespace) {
    return contactsService.getGroups(book.getId(), namespace);
  }

  @Override
  public Page<Contact> findPage(
      ContactBook book, String namespace, Pageable pageable, PropertyFilter filter) {
    return this.contactsService.findPage(
        pageable,
        filter.equal("groups.book.id", book.getId()).equal("groups.namespace", namespace));
  }

  @Override
  public Optional<Contact> findContactById(Long id) {
    return this.contactsService.findContactById(id);
  }
}
