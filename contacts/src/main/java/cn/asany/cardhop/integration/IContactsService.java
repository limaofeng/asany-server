package cn.asany.cardhop.integration;

import cn.asany.cardhop.contacts.bean.Contact;
import cn.asany.cardhop.contacts.bean.ContactBook;
import cn.asany.cardhop.contacts.bean.ContactGroup;
import cn.asany.cardhop.contacts.bean.enums.ContactBookType;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

/** 通讯录 */
public interface IContactsService {

  ContactBookType getType();

  List<ContactGroup> getGroups(ContactBook book, String namespace);

  Pager<Contact> findPager(
      ContactBook book, String namespace, Pager<Contact> pager, List<PropertyFilter> filters);

  Optional<Contact> findContactById(Long id);
}
