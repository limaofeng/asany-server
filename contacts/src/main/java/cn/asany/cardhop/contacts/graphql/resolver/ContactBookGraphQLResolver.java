package cn.asany.cardhop.contacts.graphql.resolver;

import cn.asany.base.common.Ownership;
import cn.asany.cardhop.contacts.domain.ContactBook;
import cn.asany.cardhop.contacts.domain.ContactGroup;
import cn.asany.cardhop.contacts.domain.enums.ContactBookType;
import cn.asany.cardhop.contacts.graphql.type.ContactGroupNamespace;
import cn.asany.cardhop.contacts.service.DefaultContactsServiceFactory;
import cn.asany.cardhop.contacts.utils.IdUtils;
import cn.asany.cardhop.integration.IContactsService;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 联系人分组
 *
 * @author limaofeng
 */
@Component
public class ContactBookGraphQLResolver implements GraphQLResolver<ContactBook> {

  private final DefaultContactsServiceFactory contactsServiceFactory;

  public String id(ContactBook book, DataFetchingEnvironment environment) {
    List<ContactGroupNamespace> namespaces = this.namespaces(book);
    String defaultNamespace = namespaces.get(0).getId();
    return IdUtils.toKey(book.getId(), defaultNamespace);
  }

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
