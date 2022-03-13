package cn.asany.cardhop.contacts.graphql.resolver;

import cn.asany.cardhop.contacts.bean.Contact;
import cn.asany.cardhop.contacts.utils.IdUtils;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.springframework.stereotype.Component;

@Component
public class ContactGraphQLResolver implements GraphQLResolver<Contact> {
  public String id(Contact contact, DataFetchingEnvironment environment) {
    AuthorizationGraphQLServletContext context = environment.getContext();
    IdUtils.IdKey idKey = context.getAttribute("query_contact_token");
    return IdUtils.toKey(idKey.getBook(), idKey.getNamespace(), idKey.getGroup(), contact.getId());
  }
}
