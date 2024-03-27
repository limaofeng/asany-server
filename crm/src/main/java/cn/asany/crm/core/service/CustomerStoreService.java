package cn.asany.crm.core.service;

import cn.asany.crm.core.dao.CustomerStoreDao;
import cn.asany.crm.core.domain.CustomerStore;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CustomerStoreService {

  private final CustomerStoreDao customerStoreDao;

  public CustomerStoreService(CustomerStoreDao customerStoreDao) {
    this.customerStoreDao = customerStoreDao;
  }

  public Page<CustomerStore> findPage(Pageable pageable, PropertyFilter filter) {
    return this.customerStoreDao.findPage(pageable, filter);
  }

  public List<CustomerStore> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return this.customerStoreDao.findAll(filter, offset, limit, sort);
  }

  public Optional<CustomerStore> findById(Long id) {
    return this.customerStoreDao.findById(id);
  }

  public CustomerStore save(CustomerStore customerStore) {
    return this.customerStoreDao.save(customerStore);
  }

  public CustomerStore update(Long id, CustomerStore customerStore, Boolean merge) {
    customerStore.setId(id);
    return this.customerStoreDao.update(customerStore, merge);
  }

  public CustomerStore delete(Long id) {
    CustomerStore customerStore = this.customerStoreDao.getReferenceById(id);
    this.customerStoreDao.deleteById(id);
    return customerStore;
  }
}
