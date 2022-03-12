package cn.asany.cardhop.contacts.graphql;

import cn.asany.cardhop.contacts.bean.ContactBook;
import cn.asany.cardhop.contacts.service.ContactsService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ContactsGraphqlApiResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final ContactsService contactsService;

  public ContactsGraphqlApiResolver(ContactsService contactsService) {
    this.contactsService = contactsService;
  }

  public Optional<ContactBook> contactBook(Long id) {
    return contactsService.findById(id);
  }

  public List<ContactBook> contactBooks() {
    return contactsService.findAll();
  }
}
