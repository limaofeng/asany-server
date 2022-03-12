package cn.asany.cardhop.integration;

import cn.asany.cardhop.contacts.bean.ContactBook;
import cn.asany.cardhop.contacts.bean.ContactGroup;
import cn.asany.cardhop.contacts.bean.enums.ContactBookType;
import java.util.List;

/** 通讯录 */
public interface IContactsService {

  ContactBookType getType();

  List<ContactGroup> getGroups(ContactBook book, String namespace);
}
