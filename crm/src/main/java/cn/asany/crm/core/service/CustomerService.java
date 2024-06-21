/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
