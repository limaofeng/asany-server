package cn.asany.crm.core.service;

import cn.asany.crm.core.dao.CustomerDao;
import cn.asany.crm.core.domain.Customer;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerService {
  private final CustomerDao customerDao;

  public CustomerService(CustomerDao customerDao) {
    this.customerDao = customerDao;
  }

  public Page<Customer> findPage(Pageable pageable, PropertyFilter filter) {
    return this.customerDao.findPage(pageable, filter);
  }

  public Optional<Customer> findById(Long id) {
    return this.customerDao.findById(id);
  }

  public Customer save(Customer customer) {
    return this.customerDao.save(customer);
  }

  public Customer update(Long id, Customer customer, Boolean merge) {
    customer.setId(id);
    return this.customerDao.update(customer, merge);
  }

  public List<Customer> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return this.customerDao.findAll(filter, offset, limit, sort);
  }

  public Customer delete(Long id) {
    Customer customer = this.customerDao.getReferenceById(id);
    this.customerDao.delete(customer);
    return customer;
  }
}
