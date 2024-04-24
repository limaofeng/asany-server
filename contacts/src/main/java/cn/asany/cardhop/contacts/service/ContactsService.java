package cn.asany.cardhop.contacts.service;

import cn.asany.cardhop.contacts.dao.ContactBookDao;
import cn.asany.cardhop.contacts.dao.ContactDao;
import cn.asany.cardhop.contacts.dao.ContactGroupDao;
import cn.asany.cardhop.contacts.domain.Contact;
import cn.asany.cardhop.contacts.domain.ContactBook;
import cn.asany.cardhop.contacts.domain.ContactGroup;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  public Page<Contact> findPage(Pageable pageable, PropertyFilter filter) {
    return this.contactDao.findPage(pageable, filter);
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
    return this.contactDao.getReferenceById(id);
  }

  public Optional<ContactBook> findBookById(Long id) {
    return this.contactBookDao.findById(id);
  }

  public List<ContactBook> findAll() {
    return this.contactBookDao.findAll();
  }

  public Optional<Contact> findContactById(Long contactId) {
    return this.contactDao.findById(contactId);
  }

  public List<ContactGroup> getGroups(Long book, String namespace) {
    return this.contactGroupDao.findAll(
        PropertyFilter.newFilter().equal("book.id", book).equal("namespace", namespace));
  }
}
