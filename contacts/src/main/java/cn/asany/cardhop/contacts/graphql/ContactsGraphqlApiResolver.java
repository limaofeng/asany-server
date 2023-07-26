package cn.asany.cardhop.contacts.graphql;

import cn.asany.cardhop.contacts.domain.Contact;
import cn.asany.cardhop.contacts.domain.ContactBook;
import cn.asany.cardhop.contacts.graphql.input.ContactWhereInput;
import cn.asany.cardhop.contacts.graphql.type.ContactConnection;
import cn.asany.cardhop.contacts.service.ContactsService;
import cn.asany.cardhop.contacts.service.DefaultContactsServiceFactory;
import cn.asany.cardhop.contacts.utils.IdUtils;
import cn.asany.cardhop.integration.IContactsService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class ContactsGraphqlApiResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final ContactsService contactsService;

  private final DefaultContactsServiceFactory contactsServiceFactory;

  public ContactsGraphqlApiResolver(
      ContactsService contactsService, DefaultContactsServiceFactory contactsServiceFactory) {
    this.contactsService = contactsService;
    this.contactsServiceFactory = contactsServiceFactory;
  }

  public Optional<ContactBook> contactBook(String key) {
    IdUtils.IdKey idKey = IdUtils.parseKey(key);
    return contactsService.findBookById(idKey.getBook());
  }

  public List<ContactBook> contactBooks() {
    return contactsService.findAll();
  }

  public Contact contact(String id, DataFetchingEnvironment environment) {
    IdUtils.IdKey idKey = IdUtils.parseKey(id);

    AuthorizationGraphQLServletContext context = environment.getContext();

    context.setAttribute("query_contact_token", idKey);

    Optional<ContactBook> optionalContactBook = contactsService.findBookById(idKey.getBook());

    ContactBook contactBook =
        optionalContactBook.orElseThrow(() -> new NotFoundException("通讯录不存在"));

    IContactsService service = contactsServiceFactory.getService(contactBook.getType());

    Optional<Contact> optional = service.findContactById(idKey.getContact());

    return optional.orElseThrow(() -> new NotFoundException("联系人不存在"));
  }

  public ContactConnection contacts(
      /* 筛选条件 */
      ContactWhereInput where,
      /* 偏移量 */
      int offset,
      /* 返回数据条数 */
      int first,
      /* 返回数据条数 */
      int last,
      /* 游标之后 */
      String after,
      /* 游标之前 */
      String before,
      /* 页码 */
      int page,
      /* 每页返回数据条数 */
      int pageSize,
      /* 排序 */
      Sort orderBy,
      /* 环境 */
      DataFetchingEnvironment environment) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);

    IdUtils.IdKey idKey = IdUtils.parseKey(where.getToken());

    AuthorizationGraphQLServletContext context = environment.getContext();

    context.setAttribute("query_contact_token", idKey);

    Optional<ContactBook> optionalContactBook = contactsService.findBookById(idKey.getBook());

    ContactBook contactBook =
        optionalContactBook.orElseThrow(() -> new NotFoundException("通讯录不存在"));

    IContactsService service = contactsServiceFactory.getService(contactBook.getType());

    return Kit.connection(
        service.findPage(contactBook, idKey.getNamespace(), pageable, where.toFilter()),
        ContactConnection.class);
  }
}
