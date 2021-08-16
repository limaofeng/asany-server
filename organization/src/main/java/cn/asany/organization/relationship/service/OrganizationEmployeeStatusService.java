package cn.asany.organization.relationship.service;

import cn.asany.organization.core.bean.OrganizationEmployeeStatus;
import cn.asany.organization.relationship.bean.OrganizationEmployee;
import cn.asany.organization.relationship.dao.OrganizationEmployeeDao;
import cn.asany.organization.relationship.dao.OrganizationEmployeeStatusDao;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @Annotation @Author ChenWenJie @Data 2020/7/1 5:49 下午 @Version 1.0 */
@Service
@Transactional
public class OrganizationEmployeeStatusService {
  @Autowired private OrganizationEmployeeStatusDao employeeStatusDao;
  @Autowired OrganizationEmployeeDao organizationEmployeeDao;

  public OrganizationEmployeeStatus get(Long id) {
    return this.employeeStatusDao
        .findOne(Example.of(OrganizationEmployeeStatus.builder().id(id).build()))
        .orElse(null);
  }

  public List<OrganizationEmployeeStatus> findAll(List<PropertyFilter> filter) {
    OrderBy orderBy = new OrderBy("createdAt", OrderBy.Direction.DESC);
    return this.employeeStatusDao.findAll(filter, orderBy.toSort());
  }

  public OrganizationEmployeeStatus save(OrganizationEmployeeStatus employeeStatus) {
    return this.employeeStatusDao.save(employeeStatus);
  }

  public OrganizationEmployeeStatus update(
      Long id, Boolean merge, OrganizationEmployeeStatus employeeStatus) {
    employeeStatus.setId(id);
    return this.employeeStatusDao.update(employeeStatus, merge);
  }

  public Boolean remove(Long id) {
    List<OrganizationEmployee> organizationEmployeeDaoAll =
        organizationEmployeeDao.findAll(
            Example.of(
                OrganizationEmployee.builder()
                    .status(OrganizationEmployeeStatus.builder().id(id).build())
                    .build()));
    // 组织人员状态已被员工使用则不可删除
    if (CollectionUtils.isNotEmpty(organizationEmployeeDaoAll)) {
      return false;
    }
    this.employeeStatusDao.deleteById(id);
    return true;
  }
}
