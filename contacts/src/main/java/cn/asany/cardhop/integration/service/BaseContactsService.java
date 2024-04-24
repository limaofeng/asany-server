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
