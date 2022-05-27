package cn.asany.organization.core.service;

import cn.asany.organization.core.dao.DepartmentDao;
import cn.asany.organization.core.dao.DepartmentLinkDao;
import cn.asany.organization.core.domain.Department;
import cn.asany.organization.core.domain.DepartmentLink;
import cn.asany.organization.core.domain.Job;
import cn.asany.organization.core.domain.Organization;
import cn.asany.organization.core.domain.enums.DepartmentLinkType;
import cn.asany.organization.employee.domain.Employee;
import cn.asany.organization.relationship.domain.EmployeePosition;
import cn.asany.organization.relationship.domain.Position;
import cn.asany.organization.relationship.service.PositionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 部门
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-03-11 14:50
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DepartmentService {

  private static final String PATH_SEPARATOR = "/";

  @Autowired private DepartmentDao departmentDao;
  @Autowired private DepartmentLinkDao departmentLinkDao;
  @Autowired private JobService jobService;
  @Autowired private PositionService positionService;

  public Page<Department> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return departmentDao.findPage(pageable, filters);
  }

  public Department save(Department department) {
    department = this.departmentDao.save(department);
    department.setPath(
        department.getParent() == null
            ? department.getId() + PATH_SEPARATOR
            : department.getParent().getPath() + department.getId() + PATH_SEPARATOR);
    this.departmentDao.save(department);
    return department;
  }

  public Department importsave(Department department) {
    Department dep = this.findOneByCode(department.getCode());
    if (dep == null) {
      Department parent;
      if (department.getParentId() != null) {
        parent = this.findOneByCode(department.getParentId().toString());
        if (parent != null) {
          department.setParent(parent);
          department = this.departmentDao.save(department);
          department.setPath(parent.getPath() + department.getId() + PATH_SEPARATOR);
        } else {
          Department save =
              this.importsave(
                  Department.builder()
                      .code(department.getParentId().toString())
                      .organization(department.getOrganization())
                      .build());
        }
      } else {
        department = this.departmentDao.save(department);
        department.setPath(department.getId() + PATH_SEPARATOR);
      }
      department = this.departmentDao.save(department);
    } else {
      department = this.importupdate(dep.getId(), true, department);
    }
    return department;
  }

  public Position savePosition(Long departmentId, String positionName) {
    Job job =
        jobService.save(
            Job.builder()
                .organization(this.departmentDao.getOne(departmentId).getOrganization())
                .name(positionName)
                .build());
    return positionService.save(departmentId, job.getId());
  }

  public Department get(Long id) {
    return this.departmentDao.findById(id).orElse(null);
  }

  public Department update(Long id, boolean merge, Department department) {
    department.setId(id);
    department.setPath(
        department.getParent() == null
            ? department.getId() + PATH_SEPARATOR
            : department.getParent().getPath() + department.getId() + PATH_SEPARATOR);
    return this.departmentDao.update(department, merge);
  }

  public Department importupdate(Long id, boolean merge, Department department) {
    department.setId(id);
    if (department.getParent() == null) {
      department.setPath(department.getId() + PATH_SEPARATOR);
    } else {
      Department parent = this.findOneByCode(department.getParent().getId().toString());
      department.setParent(parent);
      department.setPath(
          parent == null
              ? department.getId() + PATH_SEPARATOR
              : parent.getPath() + department.getId() + PATH_SEPARATOR);
    }
    return this.departmentDao.update(department, merge);
  }

  public void delete(Long id) {
    this.departmentDao.deleteById(id);
  }

  public Department findOneByCode(String code) {
    Optional<Department> optional =
        this.departmentDao.findOne(Example.of(Department.builder().code(code).build()));
    return optional.orElse(null);
  }

  public Department findOne(String name, Long parentId) {
    Optional<Department> optional =
        this.departmentDao.findOne(
            Example.of(
                Department.builder()
                    .name(name)
                    .parent(Department.builder().id(parentId).build())
                    .build()));
    return optional.orElse(null);
  }

  public List<Department> findAllByOrgAndDimension(Long orgId, String code) {
    List<PropertyFilter> filters =
        PropertyFilter.builder()
            .equal("organization.id", orgId)
            .equal("dimension.code", code)
            .build();
    Sort sort = Sort.by(Sort.Direction.DESC, "id");
    return this.departmentDao.findAll(filters, sort);
  }

  public List<Department> findAllByOrg(Long orgId) {
    Example example =
        Example.of(
            Department.builder().organization(Organization.builder().id(orgId).build()).build());
    Sort sort = Sort.by(Sort.Direction.DESC, "id");
    return this.departmentDao.findAll(example, sort);
  }

  public void addDepartmentLink(DepartmentLinkType type, Long id, String linkId) {
    Optional<DepartmentLink> optional =
        this.departmentLinkDao.findOne(
            Example.of(
                DepartmentLink.builder()
                    .department(Department.builder().id(id).build())
                    .type(type)
                    .build()));
    if (optional.isPresent()) {
      DepartmentLink link = optional.get();
      link.setLinkId(linkId);
      this.departmentLinkDao.update(link, false);
    } else {
      DepartmentLink link =
          DepartmentLink.builder()
              .type(type)
              .department(Department.builder().id(id).build())
              .linkId(linkId)
              .build();
      this.departmentLinkDao.save(link);
    }
  }

  public List<Department> departments(List<PropertyFilter> filters) {
    Sort sort = Sort.by(Sort.Direction.ASC, "sort");
    return departmentDao.findAll(filters, sort);
  }

  public List<Department> departmentsByPath(String path) {
    PropertyFilterBuilder builder = new PropertyFilterBuilder();
    builder.contains("path", path + "%");
    return departmentDao.findAll(builder.build());
  }

  /**
   * 根据部门ID查询所有下级组织（包括所有子集）,falg未true表示查询数据包括自己，未false表示查询数据不包括自己
   *
   * @param id
   * @param flag
   * @return
   */
  @Deprecated
  public List<Department> findChildrens(Long id, Boolean flag) {
    PropertyFilterBuilder builder = new PropertyFilterBuilder();
    builder.contains("path", id + "%");
    if (!flag) {
      builder.isNotNull("parent.id");
    }
    return departmentDao.findAll(builder.build());
  }

  /**
   * 根据部门ID查询所有下级组织
   *
   * @return
   */
  @Deprecated
  public List<Department> findPaths(Long departmentId) {
    PropertyFilterBuilder builder = new PropertyFilterBuilder();
    builder.contains("path", departmentId + "%");
    return departmentDao.findAll(builder.build());
  }

  /**
   * 根据组织ID查询所有一级部门
   *
   * @param orgid
   * @return
   */
  @Deprecated
  public List<Department> findLevel1Department(String orgid) {
    PropertyFilterBuilder builder = new PropertyFilterBuilder();
    builder.equal("organization.id", orgid);
    builder.isNull("parent.id");
    return departmentDao.findAll(builder.build());
  }

  public List<Department> findAll(List<PropertyFilter> filters) {
    return this.departmentDao.findAll(filters);
  }

  /**
   * 根据医院名获取对应的ID如果没有匹配时则返回null
   *
   * @param departmentName
   * @return
   */
  public Department getDepartmentByName(String departmentName, Long organization) {
    return this.departmentDao
        .findOne(
            Example.of(
                Department.builder()
                    .name(departmentName)
                    .organization(Organization.builder().id(organization).build())
                    .build()))
        .orElse(null);
  }

  /**
   * 根据部门名和组织id模糊查询部门
   *
   * @param departmentName
   * @param organization
   * @return
   */
  public List<Department> getDepartmentsByName(String departmentName, Long organization) {
    PropertyFilterBuilder builder = new PropertyFilterBuilder();
    builder.contains("name", departmentName);
    builder.equal("organization.id", organization);
    return this.departmentDao.findAll(builder.build());
  }

  /**
   * 查询子部门
   *
   * @param build
   * @return
   */
  public List<Department> findChildren(List<PropertyFilter> build) {
    return departmentDao.findAll(build);
  }

  /**
   * 查询子部门数
   *
   * @param build
   * @return
   */
  public Long findChildrenCount(List<PropertyFilter> build) {
    return departmentDao.count(build);
  }

  public List<Employee> findChildrenDepartmentEmployee(Department department) {
    List<Department> all =
        departmentDao.findAll(Example.of(Department.builder().parent(department).build()));
    List<Employee> employeeList = new ArrayList<>();
    all.stream()
        .forEach(
            item -> item.getEmployees().stream().forEach(it -> employeeList.add(it.getEmployee())));
    return employeeList;
  }

  public List<Employee> getEmployee(List<EmployeePosition> employeePositions) {
    List<Employee> employees = new ArrayList<>();
    if (employeePositions.size() > 0) {
      for (EmployeePosition employeePosition : employeePositions) {
        employees.add(employeePosition.getEmployee());
      }
    }
    return employees;
  }
}
