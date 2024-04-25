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
package cn.asany.organization.core.service;

import cn.asany.organization.core.dao.EmployeeGroupDao;
import cn.asany.organization.core.dao.EmployeeGroupScopeDao;
import cn.asany.organization.core.domain.EmployeeGroup;
import cn.asany.organization.core.domain.EmployeeGroupScope;
import cn.asany.organization.core.domain.Organization;
import cn.asany.organization.employee.dao.EmployeeDao;
import cn.asany.organization.employee.domain.Employee;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeGroupService {

  @Autowired private EmployeeGroupDao employeeGroupDao;
  @Autowired private EmployeeGroupScopeDao employeeGroupScopeDao;
  @Autowired private EmployeeDao employeeDao;

  public EmployeeGroup save(EmployeeGroup userGroup, String businessId) {
    EmployeeGroup employeeGroup = this.employeeGroupDao.save(userGroup);
    return employeeGroup;
  }

  public EmployeeGroup update(Long id, boolean merge, EmployeeGroup userGroup) {
    userGroup.setId(id);
    return this.employeeGroupDao.update(userGroup, merge);
  }

  /**
   * 向组中添加用户
   *
   * @param employees
   * @param group
   */
  public void addEmployee(List<Long> employees, Long group, String businessId) {
    EmployeeGroup employeeGroup = this.employeeGroupDao.getOne(group);
    if (employees.size() <= 0) {
      return;
    }
    //        updateBusinessEmployeeGroup(businessId, employeeGroup, employees);
    for (Long employee : employees) {
      PropertyFilter filter = PropertyFilter.newFilter();
      filter.equal("employees.id", employee).equal("id", group);
      List ListEmployee = employeeGroupDao.findAll(filter);
      if (ListEmployee.size() > 0) {
        continue;
      }
      employeeGroup.getEmployees().add(Employee.builder().id(employee).build());
      this.employeeGroupDao.save(employeeGroup);
    }
  }

  public List<EmployeeGroup> groups(String scope) {
    EmployeeGroup.EmployeeGroupBuilder builder = EmployeeGroup.builder();
    if (StringUtil.isNotBlank(scope)) {
      builder.scope(EmployeeGroupScope.builder().id(scope).build());
    }
    return this.employeeGroupDao.findAll(Example.of(builder.build()));
  }

  public List<EmployeeGroup> groups(Long organization, String scope, String name) {
    EmployeeGroup.EmployeeGroupBuilder builder = EmployeeGroup.builder();
    if (StringUtil.isNotBlank(organization)) {
      builder.scope(
          EmployeeGroupScope.builder()
              .organization(Organization.builder().id(organization).build())
              .build());
    }
    if (StringUtil.isNotBlank(scope)) {
      builder.scope(EmployeeGroupScope.builder().id(scope).build());
    }
    if (StringUtil.isNotBlank(name)) {
      builder.name(name);
    }
    List<EmployeeGroup> all = new ArrayList<>();
    if (name != null && scope != null) {
      all = this.employeeGroupDao.findEmployeeGroups(organization, scope, name);
    } else {
      all = this.employeeGroupDao.findAll(Example.of(builder.build()));
    }
    //    all.forEach(
    //        po ->
    //            po.getEmployees()
    //                .forEach(
    //                    vo ->
    //                        vo.setCurrentOrganization(
    //                            Organization.builder().id(organization).build())));
    return all;
  }

  public EmployeeGroup get(Long id) {
    return this.employeeGroupDao.findById(id).orElse(null);
  }

  public void delete(Long id) {
    this.employeeGroupDao.deleteById(id);
  }

  public List<EmployeeGroupScope> findScopes(Long organization) {
    Example example =
        Example.of(
            EmployeeGroupScope.builder()
                .organization(Organization.builder().id(organization).build())
                .build());
    return this.employeeGroupScopeDao.findAll(example);
  }

  public List<EmployeeGroupScope> findScopes() {
    return this.employeeGroupScopeDao.findAll();
  }

  public void removeEmployeeToGroup(Long employeeId, Long group, String businessId) {
    Employee employee = this.employeeDao.getOne(employeeId);
    ObjectUtil.remove(employee.getGroups(), "id", group);
    Optional<EmployeeGroup> employeeGroup = employeeGroupDao.findById(group);
    if (employeeGroup.isPresent()) {
      List<Employee> employees = employeeGroup.get().getEmployees();
      List<Long> list = new ArrayList<>();
      employees.stream().forEach(item -> list.add(item.getId()));
    }

    this.employeeDao.save(employee);
  }
}
