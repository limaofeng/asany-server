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
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Annotation 群组类型 @ClassName EmployeeGroupScopeService @Author ChenWenJie @Data 2020/7/8 11:38
 * 上午 @Version 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeGroupScopeService {
  @Autowired private EmployeeGroupScopeDao employeeGroupScopeDao;
  @Autowired private EmployeeGroupDao employeeGroupDao;

  public EmployeeGroupScope save(EmployeeGroupScope groupScope) {
    return employeeGroupScopeDao.save(groupScope);
  }

  public EmployeeGroupScope update(EmployeeGroupScope groupScope, boolean merge) {
    return employeeGroupScopeDao.update(groupScope, merge);
  }

  public List<EmployeeGroupScope> findAll(Long organization, String name) {
    PropertyFilter filter = PropertyFilter.newFilter();
    filter.equal("organization.id", organization);
    if (StringUtils.isNotBlank(name)) {
      filter.contains("name", name);
    }
    return employeeGroupScopeDao.findAll(filter);
  }

  public EmployeeGroupScope get(String id) {
    return employeeGroupScopeDao.findById(id).orElse(null);
  }

  public boolean delete(String id) {
    List<EmployeeGroup> groupList =
        employeeGroupDao.findAll(
            Example.of(
                EmployeeGroup.builder()
                    .scope(EmployeeGroupScope.builder().id(id).build())
                    .build()));
    if (CollectionUtils.isEmpty(groupList)) {
      employeeGroupScopeDao.deleteById(id);
      return true;
    }
    return false;
  }
}
