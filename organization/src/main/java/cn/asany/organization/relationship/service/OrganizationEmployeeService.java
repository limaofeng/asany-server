package cn.asany.organization.relationship.service;

import cn.asany.organization.core.dao.JobDao;
import cn.asany.organization.employee.dao.EmployeeDao;
import cn.asany.organization.employee.dao.EmployeePositionDao;
import cn.asany.organization.relationship.bean.OrganizationEmployee;
import cn.asany.organization.relationship.dao.OrganizationEmployeeDao;
import cn.asany.organization.relationship.dao.OrganizationEmployeeStatusDao;
import cn.asany.organization.relationship.dao.PositionDao;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @Author ruanjj @Date 2019/11/13 15:54 @Description TODO */
@Service
@Transactional
public class OrganizationEmployeeService {

  @Autowired OrganizationEmployeeDao organizationEmployeeDao;
  @Autowired OrganizationEmployeeStatusDao organizationEmployeeStatusDao;
  @Autowired PositionDao positionDao;
  @Autowired EmployeeDao employeeDao;
  @Autowired EmployeePositionDao employeePositionDao;
  @Autowired JobDao jobDao;

  public Optional<OrganizationEmployee> get(String id, Long id1) {
    return null;
  }
}
