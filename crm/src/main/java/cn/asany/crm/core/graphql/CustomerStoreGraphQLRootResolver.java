package cn.asany.crm.core.graphql;

import cn.asany.crm.core.convert.CustomerStoreConverter;
import cn.asany.crm.core.domain.CustomerStore;
import cn.asany.crm.core.graphql.input.CustomerStoreCreateInput;
import cn.asany.crm.core.graphql.input.CustomerStoreUpdateInput;
import cn.asany.crm.core.graphql.input.CustomerStoreWhereInput;
import cn.asany.crm.core.graphql.type.CustomerStoreConnection;
import cn.asany.crm.core.service.CustomerStoreService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CustomerStoreGraphQLRootResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final CustomerStoreService customerStoreService;
  private final CustomerStoreConverter customerStoreConverter;

  public CustomerStoreGraphQLRootResolver(
      CustomerStoreService customerStoreService, CustomerStoreConverter customerStoreConverter) {
    this.customerStoreService = customerStoreService;
    this.customerStoreConverter = customerStoreConverter;
  }

  public CustomerStoreConnection customerStoresConnection(
      CustomerStoreWhereInput where, int currentPage, int pageSize, Sort sort) {
    PropertyFilter filter = where.toFilter();
    Page<CustomerStore> page =
        customerStoreService.findPage(PageRequest.of(currentPage - 1, pageSize, sort), filter);
    return Kit.connection(page, CustomerStoreConnection.class);
  }

  public List<CustomerStore> customerStores(
      CustomerStoreWhereInput where, int offset, int limit, Sort sort) {
    PropertyFilter filter = where.toFilter();
    return customerStoreService.findAll(filter, offset, limit, sort);
  }

  public Optional<CustomerStore> customerStore(Long id) {
    return customerStoreService.findById(id);
  }

  public CustomerStore createCustomerStore(CustomerStoreCreateInput input) {
    CustomerStore customerStore = customerStoreConverter.toCustomerStore(input);
    return customerStoreService.save(customerStore);
  }

  public CustomerStore updateCustomerStore(Long id, CustomerStoreUpdateInput input, Boolean merge) {
    CustomerStore customerStore = customerStoreConverter.toCustomerStore(input);
    return customerStoreService.update(id, customerStore, merge);
  }

  public CustomerStore deleteCustomerStore(Long id) {
    return customerStoreService.delete(id);
  }
}
