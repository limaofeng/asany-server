package cn.asany.organization.core.service;

import cn.asany.organization.core.dao.EmployeeGroupDao;
import cn.asany.organization.core.dao.EmployeeGroupScopeDao;
import cn.asany.organization.core.domain.EmployeeGroup;
import cn.asany.organization.core.domain.EmployeeGroupScope;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
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
