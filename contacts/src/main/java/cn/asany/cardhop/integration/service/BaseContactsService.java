package cn.asany.cardhop.integration.service;

import cn.asany.cardhop.contacts.bean.Contact;
import cn.asany.cardhop.contacts.bean.ContactBook;
import cn.asany.cardhop.contacts.bean.ContactGroup;
import cn.asany.cardhop.contacts.bean.enums.ContactBookType;
import cn.asany.cardhop.contacts.service.ContactsService;
import cn.asany.cardhop.integration.IContactsService;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.springframework.stereotype.Service;

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
  public Pager<Contact> findPager(
      ContactBook book, String namespace, Pager<Contact> pager, List<PropertyFilter> filters) {
    PropertyFilterBuilder builder = new PropertyFilterBuilder(filters);
    return this.contactsService.findPager(
        pager,
        builder.equal("groups.book.id", book.getId()).equal("groups.namespace", namespace).build());
  }

  @Override
  public Optional<Contact> findContactById(Long id) {
    return this.contactsService.findContactById(id);
  }
}
