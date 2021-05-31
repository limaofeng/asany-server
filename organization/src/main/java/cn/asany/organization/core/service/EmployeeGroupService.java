package cn.asany.organization.core.service;

import cn.asany.organization.core.bean.EmployeeGroup;
import cn.asany.organization.core.bean.EmployeeGroupScope;
import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.employee.bean.Employee;
import cn.asany.organization.employee.dao.EmployeeDao;
import cn.asany.organization.core.dao.EmployeeGroupDao;
import cn.asany.organization.core.dao.EmployeeGroupScopeDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 组
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-06-17 15:54
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeGroupService {

    @Autowired
    private EmployeeGroupDao employeeGroupDao;
    @Autowired
    private EmployeeGroupScopeDao employeeGroupScopeDao;
    @Autowired
    private EmployeeDao employeeDao;

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
            List<PropertyFilter> list = new ArrayList();
            list.add(new PropertyFilter("EQ_employees.id", employee));
            list.add(new PropertyFilter("EQ_id", group));
            List ListEmployee = employeeGroupDao.findAll(list);
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


    public List<EmployeeGroup> groups(String organization, String scope, String name) {
        EmployeeGroup.EmployeeGroupBuilder builder = EmployeeGroup.builder();
        if (StringUtil.isNotBlank(organization)) {
            builder.scope(EmployeeGroupScope.builder().organization(Organization.builder().id(organization).build()).build());
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
        all.forEach(po -> po.getEmployees().forEach(vo -> vo.setCurrentOrganization(Organization.builder().id(organization).build())));
        return all;
    }

    public EmployeeGroup get(Long id) {
        return this.employeeGroupDao.findById(id).orElse(null);
    }

    public void delete(Long id) {
        this.employeeGroupDao.deleteById(id);
    }

    public List<EmployeeGroupScope> findScopes(String organization) {
        Example example = Example.of(EmployeeGroupScope.builder()
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

    public List<EmployeeGroup> findEmployeeGroupData(List<PropertyFilter> filters) {
        Pager<EmployeeGroup> objectPager = new Pager<>();
        objectPager.setPageSize(10000);
        Pager<EmployeeGroup> pager = employeeGroupDao.findPager(objectPager, filters);
        List<EmployeeGroup> pageItems = pager.getPageItems();
        return pageItems;
    }

}
