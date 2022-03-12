package cn.asany.cardhop.contacts.graphql.resolver;

import cn.asany.base.common.Ownership;
import cn.asany.cardhop.contacts.bean.ContactBook;
import cn.asany.cardhop.contacts.bean.ContactGroup;
import cn.asany.cardhop.contacts.bean.enums.ContactBookType;
import cn.asany.cardhop.contacts.graphql.type.ContactGroupNamespace;
import cn.asany.cardhop.contacts.service.DefaultContactsServiceFactory;
import cn.asany.cardhop.integration.IContactsService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ContactBookGraphQLResolver implements GraphQLResolver<ContactBook> {

  private final DefaultContactsServiceFactory contactsServiceFactory;

  public ContactBookGraphQLResolver(DefaultContactsServiceFactory contactsServiceFactory) {
    this.contactsServiceFactory = contactsServiceFactory;
  }

  public List<ContactGroupNamespace> namespaces(ContactBook book) {
    if (ContactBookType.ENTERPRISE == book.getType()) {
      return ContactGroupNamespace.Enterprise;
    }
    return ContactGroupNamespace.Cardhop;
  }

  public List<ContactGroup> groups(ContactBook book, String namespace) {
    Ownership ownership = book.getOwner();

    IContactsService contactsService = contactsServiceFactory.getService(book.getType());

    if (contactsService == null) {
      return book.getGroups();
    }

    return contactsService.getGroups(book, namespace);
  }
}
