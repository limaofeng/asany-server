package cn.asany.organization.core.service;

import cn.asany.organization.core.bean.EmployeeStatus;
import cn.asany.organization.employee.dao.EmployeeStatusDao;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @Annotation @Author ChenWenJie @Data 2020/7/1 5:49 下午 @Version 1.0 */
@Service
@Transactional
public class EmployeeStatusService {
  @Autowired private EmployeeStatusDao employeeStatusDao;

  public EmployeeStatus get(Long id) {
    return this.employeeStatusDao
        .findOne(Example.of(EmployeeStatus.builder().id(id).build()))
        .orElse(null);
  }

  public List<EmployeeStatus> findAll(List<PropertyFilter> filter) {
    return this.employeeStatusDao.findAll(filter, Sort.by("createdAt").descending());
  }

  public EmployeeStatus save(EmployeeStatus employeeStatus) {
    return this.employeeStatusDao.save(employeeStatus);
  }

  public EmployeeStatus update(Long id, Boolean merge, EmployeeStatus employeeStatus) {
    employeeStatus.setId(id);
    return this.employeeStatusDao.update(employeeStatus, merge);
  }

  public Boolean remove(Long id) {
    this.employeeStatusDao.deleteById(id);
    return true;
  }
}
