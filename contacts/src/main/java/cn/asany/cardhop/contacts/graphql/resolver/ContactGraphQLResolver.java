package cn.asany.cardhop.contacts.graphql.resolver;

import cn.asany.base.common.domain.Address;
import cn.asany.base.common.domain.Email;
import cn.asany.base.common.domain.Phone;
import cn.asany.cardhop.contacts.domain.Contact;
import cn.asany.cardhop.contacts.domain.ContactAddress;
import cn.asany.cardhop.contacts.domain.ContactEmail;
import cn.asany.cardhop.contacts.domain.ContactPhoneNumber;
import cn.asany.cardhop.contacts.utils.IdUtils;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.Optional;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.graphql.security.context.AuthGraphQLServletContext;
import org.springframework.stereotype.Component;

@Component
public class ContactGraphQLResolver implements GraphQLResolver<Contact> {
  public String id(Contact contact, DataFetchingEnvironment environment) {
    AuthGraphQLServletContext context = environment.getContext();
    IdUtils.IdKey idKey = context.getAttribute("query_contact_token");
    return IdUtils.toKey(idKey.getBook(), idKey.getNamespace(), idKey.getGroup(), contact.getId());
  }

  public Optional<Phone> phone(Contact contact, String label) {
    ContactPhoneNumber phoneNumber =
        ObjectUtil.find(
            contact.getPhones(),
            (item) -> {
              if (StringUtil.isBlank(label)) {
                return item.getPrimary();
              } else {
                return label.equals(item.getLabel());
              }
            });
    if (phoneNumber == null) {
      return Optional.empty();
    }
    return Optional.of(phoneNumber.getPhone());
  }

  public Optional<Email> email(Contact contact, String label) {
    ContactEmail contactEmail =
        ObjectUtil.find(
            contact.getEmails(),
            (item) -> {
              if (StringUtil.isBlank(label)) {
                return item.getPrimary();
              } else {
                return label.equals(item.getLabel());
              }
            });
    if (contactEmail == null) {
      return Optional.empty();
    }
    return Optional.of(contactEmail.getEmail());
  }

  public Optional<Address> address(Contact contact, String label) {
    ContactAddress contactAddress =
        ObjectUtil.find(
            contact.getAddresses(),
            (item) -> {
              if (StringUtil.isBlank(label)) {
                return item.getPrimary();
              } else {
                return label.equals(item.getLabel());
              }
            });
    if (contactAddress == null) {
      return Optional.empty();
    }
    return Optional.of(contactAddress.getAddress());
  }
}
