package cn.asany.cardhop.contacts.service;

import cn.asany.cardhop.contacts.bean.Contact;
import cn.asany.cardhop.contacts.bean.ContactBook;
import cn.asany.cardhop.contacts.bean.ContactGroup;
import cn.asany.cardhop.contacts.dao.ContactBookDao;
import cn.asany.cardhop.contacts.dao.ContactDao;
import cn.asany.cardhop.contacts.dao.ContactGroupDao;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContactsService {

  @Autowired private ContactBookDao contactBookDao;
  @Autowired private ContactGroupDao contactGroupDao;
  @Autowired private ContactDao contactDao;

  //    public AddressBook getAddressBook(String owner, String ownerType) {
  //        Book book = bookDao.findUnique(Restrictions.eq("ownerType", ownerType),
  // Restrictions.eq("owner", owner));
  //        if (book == null) {
  //            book = new Book(owner, ownerType);
  //            book = bookDao.save(book);
  //        }
  //        return new AddressBook(book);
  //    }

  public Pager<Contact> findPager(Pager<Contact> pager, List<PropertyFilter> filters) {
    return this.contactDao.findPager(pager, filters);
  }

  //    public AddressBook myBook(String username) {
  //        return getAddressBook(username, "1");
  //    }

  public ContactGroup save(ContactGroup group) {
    return contactGroupDao.save(group);
  }

  public void deleteGroup(Long... ids) {
    for (Long id : ids) {
      contactGroupDao.deleteById(id);
    }
  }

  public Contact save(Contact contact) {
    return contactDao.save(contact);
  }

  public void deleteContact(Long... ids) {
    for (Long id : ids) {
      contactDao.deleteById(id);
    }
  }

  public Contact get(Long id) {
    return this.contactDao.getById(id);
  }

  public Optional<ContactBook> findById(Long id) {
    return this.contactBookDao.findById(id);
  }

  public List<ContactBook> findAll() {
    return this.contactBookDao.findAll();
  }

  //    public static List<Group> getGroups() {
  //        return ((AddressBook)
  // SpringSecurityUtils.getCurrentUser(SimpleUser.class).data(AddressBookListener.CURRENT_USER_BOOK_KEY)).getGroups();
  //    }

}
