package cn.asany.crm.core.graphql;

import cn.asany.crm.core.convert.CustomerConverter;
import cn.asany.crm.core.domain.Customer;
import cn.asany.crm.core.graphql.input.CustomerCreateInput;
import cn.asany.crm.core.graphql.input.CustomerUpdateInput;
import cn.asany.crm.core.graphql.input.CustomerWhereInput;
import cn.asany.crm.core.graphql.type.CustomerConnection;
import cn.asany.crm.core.service.CustomerService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CustomerGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final CustomerService customerService;

  private final CustomerConverter customerConverter;

  public CustomerGraphQLRootResolver(
      CustomerService customerService, CustomerConverter customerConverter) {
    this.customerService = customerService;
    this.customerConverter = customerConverter;
  }

  public CustomerConnection customersConnection(
      CustomerWhereInput where, int currentPage, int pageSize, Sort sort) {
    PropertyFilter filter = where.toFilter();
    Page<Customer> page =
        customerService.findPage(PageRequest.of(currentPage - 1, pageSize, sort), filter);
    return Kit.connection(page, CustomerConnection.class);
  }

  public List<Customer> customers(CustomerWhereInput where, int offset, int limit, Sort sort) {
    PropertyFilter filter = where.toFilter();
    return customerService.findAll(filter, offset, limit, sort);
  }

  public Optional<Customer> customer(Long id) {
    return customerService.findById(id);
  }

  public Customer createCustomer(CustomerCreateInput input) {
    Customer customer = customerConverter.toCustomer(input);
    return customerService.save(customer);
  }

  public Customer updateCustomer(Long id, CustomerUpdateInput input, Boolean merge) {
    Customer customer = customerConverter.toCustomer(input);
    return customerService.update(id, customer, merge);
  }

  public Customer deleteCustomer(Long id) {
    return customerService.delete(id);
  }
}
