package cn.asany.cardhop.contacts.graphql.resolver;

import cn.asany.base.common.bean.Address;
import cn.asany.base.common.bean.Email;
import cn.asany.base.common.bean.Phone;
import cn.asany.cardhop.contacts.bean.Contact;
import cn.asany.cardhop.contacts.bean.ContactAddress;
import cn.asany.cardhop.contacts.bean.ContactEmail;
import cn.asany.cardhop.contacts.bean.ContactPhoneNumber;
import cn.asany.cardhop.contacts.utils.IdUtils;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.Optional;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.springframework.stereotype.Component;

@Component
public class ContactGraphQLResolver implements GraphQLResolver<Contact> {
  public String id(Contact contact, DataFetchingEnvironment environment) {
    AuthorizationGraphQLServletContext context = environment.getContext();
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
