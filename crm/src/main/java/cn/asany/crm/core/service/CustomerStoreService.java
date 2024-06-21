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

import cn.asany.crm.core.dao.CustomerStoreDao;
import cn.asany.crm.core.domain.CustomerStore;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
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
