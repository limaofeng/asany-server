package cn.asany.organization.core.service;

import cn.asany.organization.core.convert.NuwaOrganizationConverter;
import cn.asany.organization.core.dao.*;
import cn.asany.organization.core.domain.*;
import cn.asany.organization.core.domain.enums.MemberRole;
import cn.asany.organization.core.service.dto.NuwaOrgDimension;
import cn.asany.organization.core.service.dto.NuwaOrganization;
import cn.asany.organization.employee.dao.EmployeeDao;
import cn.asany.organization.employee.dao.EmployeeEmailDao;
import cn.asany.organization.employee.dao.EmployeePhoneDao;
import cn.asany.organization.employee.dao.EmployeeStatusDao;
import cn.asany.organization.employee.domain.Employee;
import cn.asany.organization.employee.domain.EmployeeEmail;
import cn.asany.organization.employee.domain.EmployeePhoneNumber;
import cn.asany.organization.relationship.dao.EmployeePositionDao;
import cn.asany.organization.relationship.dao.PositionDao;
import cn.asany.organization.relationship.domain.EmployeePosition;
import cn.asany.organization.relationship.domain.Position;
import java.util.*;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.reflect.Property;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织服务
 *
 * @author limaofeng
 * @version V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrganizationService {

  private final NuwaOrganizationConverter nuwaOrganizationConverter;
  private final OrganizationDao organizationDao;
  private final EmployeeStatusDao employeeStatusDao;
  private final OrganizationDimensionDao organizationDimensionDao;
  private final DepartmentTypeDao departmentTypeDao;
  private final DepartmentDao departmentDao;
  private final EmployeeDao employeeDao;
  private final EmployeeIdentityDao employeeIdentityDao;
  private final EmployeeEmailDao employeeEmailDao;
  private final EmployeePhoneDao employeePhoneDao;
  private final TeamMemberDao teamMemberDao;
  private final PositionDao positionDao;
  private final EmployeePositionDao employeePositionDao;

  public OrganizationService(
      EmployeeDao employeeDao,
      OrganizationDao organizationDao,
      EmployeeIdentityDao employeeIdentityDao,
      EmployeeStatusDao employeeStatusDao,
      OrganizationDimensionDao organizationDimensionDao,
      NuwaOrganizationConverter nuwaOrganizationConverter,
      DepartmentTypeDao departmentTypeDao,
      DepartmentDao departmentDao,
      EmployeeEmailDao employeeEmailDao,
      EmployeePhoneDao employeePhoneDao,
      TeamMemberDao teamMemberDao,
      PositionDao positionDao,
      EmployeePositionDao employeePositionDao) {
    this.employeeDao = employeeDao;
    this.employeeIdentityDao = employeeIdentityDao;
    this.organizationDao = organizationDao;
    this.employeeStatusDao = employeeStatusDao;
    this.organizationDimensionDao = organizationDimensionDao;
    this.nuwaOrganizationConverter = nuwaOrganizationConverter;
    this.departmentTypeDao = departmentTypeDao;
    this.departmentDao = departmentDao;
    this.employeeEmailDao = employeeEmailDao;
    this.employeePhoneDao = employeePhoneDao;
    this.teamMemberDao = teamMemberDao;
    this.positionDao = positionDao;
    this.employeePositionDao = employeePositionDao;
  }

  public Organization createOrganization(NuwaOrganization org) {
    return importOrganization(org, true);
  }

  public Organization importOrganization(NuwaOrganization org, boolean isNew) {
    Optional<Organization> optionalOrganization =
        this.organizationDao.findOneBy("code", org.getCode());

    if (optionalOrganization.isPresent() && isNew) {
      throw new RuntimeException("组织已经存在");
    }
    if (!optionalOrganization.isPresent()) {
      isNew = true;
      optionalOrganization =
          Optional.of(
              this.organizationDao.save(
                  Organization.builder().code(org.getCode()).name(org.getName()).build()));
    }
    Organization organization = optionalOrganization.get();

    List<OrganizationDimension> dimensions =
        this.nuwaOrganizationConverter.toDimensions(org.getDimensions());

    for (int i = 0; i < dimensions.size(); i++) {
      OrganizationDimension dimension = dimensions.get(i);
      Optional<OrganizationDimension> optionalDimension =
          isNew
              ? Optional.empty()
              : this.organizationDimensionDao.findOneBy("code", dimension.getCode());
      if (!optionalDimension.isPresent()) {
        dimension.setOrganization(organization);
        optionalDimension = Optional.of(this.organizationDimensionDao.save(dimension));
      } else {
        OrganizationDimension oldObj = optionalDimension.get();
        oldObj.setName(oldObj.getName());
        this.organizationDimensionDao.update(oldObj);
      }
      dimensions.set(i, dimension = optionalDimension.get());

      NuwaOrgDimension nuwaOrgDimension =
          ObjectUtil.find(org.getDimensions(), "code", dimension.getCode());

      List<EmployeeStatus> statuses = nuwaOrgDimension.getStatuses();
      dimension.setStatuses(saveEmployeeStatuses(dimension, statuses, isNew));

      List<DepartmentType> departmentTypes = nuwaOrgDimension.getDepartmentTypes();
      saveDepartmentTypes(dimension, departmentTypes, isNew);

      List<Department> departments =
          this.nuwaOrganizationConverter.toDepartments(nuwaOrgDimension.getDepartments());

      dimension.setDepartments(saveDepartments(dimension, departmentTypes, departments, isNew));
    }

    List<Employee> employees = this.nuwaOrganizationConverter.toEmployees(org.getEmployees());

    saveEmployees(organization, employees, dimensions);

    return organization;
  }

  private void saveEmployees(
      Organization organization, List<Employee> employees, List<OrganizationDimension> dimensions) {
    List<EmployeePhoneNumber> employeePhoneNumbers = new ArrayList<>();
    List<EmployeeEmail> employeeEmails = new ArrayList<>();
    List<EmployeeIdentity> employeeIdentities = new ArrayList<>();
    List<EmployeePosition> employeePositions = new ArrayList<>();
    Set<Position> positions = new HashSet<>();
    for (Employee employee : employees) {
      employee.setOrganization(organization);
      for (EmployeeIdentity identity : employee.getIdentities()) {
        OrganizationDimension dimension =
            ObjectUtil.find(dimensions, "name", identity.getDimension().getName());
        identity.setDimension(dimension);
        identity.setOrganization(dimension.getOrganization());
        identity.setEmployee(employee);

        List<Department> departmentTree =
            ObjectUtil.tree(dimension.getDepartments(), "id", "parent.id", "children");

        EmployeeStatus status =
            ObjectUtil.find(dimension.getStatuses(), "name", identity.getStatus().getName());
        identity.setStatus(status);

        Department department =
            this.findDepartmentByName(
                departmentTree, identity.getDepartment().getName().split("/"));

        Position position = this.findPositionByName(department, identity.getPosition().getName());

        if (position.getId() == null) {
          positions.add(position);
        }

        identity.setDepartment(department);
        identity.setPosition(position);

        employeeIdentities.add(identity);

        employeePositions.add(
            EmployeePosition.builder()
                .employee(employee)
                .department(department)
                .position(position)
                .primary(true)
                .organization(organization)
                .dimension(dimension)
                .build());

        for (EmployeePhoneNumber phoneNumber : employee.getPhones()) {
          phoneNumber.setEmployee(employee);
          employeePhoneNumbers.add(phoneNumber);
        }
        if (!employee.getPhones().isEmpty()) {
          employee.getPhones().get(0).setPrimary(true);
        }

        for (EmployeeEmail employeeEmail : employee.getEmails()) {
          employeeEmail.setEmployee(employee);
          employeeEmails.add(employeeEmail);
        }
        if (!employee.getEmails().isEmpty()) {
          employee.getEmails().get(0).setPrimary(true);
        }
      }
      System.out.println(employee);
    }
    this.employeeDao.saveAllInBatch(employees);
    this.employeePhoneDao.saveAllInBatch(employeePhoneNumbers);
    this.employeeEmailDao.saveAllInBatch(employeeEmails);
    this.positionDao.saveAllInBatch(positions);
    this.employeeIdentityDao.saveAllInBatch(employeeIdentities);
    this.employeePositionDao.saveAllInBatch(employeePositions);
  }

  /**
   * 导入时，会根据名称自动创建职位，但职位名称必须在 父级中定义过
   *
   * @param department 部门
   * @param name 名称
   * @return boolean
   */
  private boolean verifyPosition(Department department, String name) {
    Position position = ObjectUtil.find(department.getPositions(), "name", name);
    if (position == null) {
      return department.getParent() != null && verifyPosition(department.getParent(), name);
    }
    return true;
  }

  private Position findPositionByName(Department department, String name) {
    if (!verifyPosition(department, name)) {
      throw new RuntimeException("职位[" + name + "]未定义");
    }
    Position position = ObjectUtil.find(department.getPositions(), "name", name);
    if (position == null) {
      position =
          Position.builder()
              .name(name)
              .department(department)
              .organization(department.getOrganization())
              .build();
      department.getPositions().add(position);
      return position;
    }
    return position;
  }

  private Department findDepartmentByName(List<Department> departmentTree, String[] names) {
    List<Department> root = departmentTree;
    Department department = null;
    for (String name : names) {
      department = ObjectUtil.find(root, "name", name);
      root = department.getChildren();
    }
    return department;
  }

  private List<Department> saveDepartments(
      OrganizationDimension dimension,
      List<DepartmentType> departmentTypes,
      List<Department> departments,
      boolean isNewOrg) {

    List<Position> positions = new ArrayList<>();
    List<Department> oldDepartments =
        isNewOrg
            ? new ArrayList<>()
            : this.departmentDao.findAll(
                PropertyFilter.builder().equal("dimension.id", dimension.getId()).build());

    departments =
        ObjectUtil.recursive(
            departments,
            (item, context) -> {
              Department old =
                  isNewOrg ? null : ObjectUtil.find(oldDepartments, "code", item.getCode());
              if (old != null) {
                old.setName(item.getName());
                old.setType(item.getType());
                item = old;
              }

              item.setIndex(context.getIndex());
              item.setLevel(context.getLevel());

              item.setParent(context.getParent());
              item.setOrganization(dimension.getOrganization());
              item.setDimension(dimension);

              if (item.getType() != null) {
                item.setType(ObjectUtil.find(departmentTypes, "name", item.getType().getName()));
              } else {
                item.setType(item.getParent().getType());
              }

              for (Position p : item.getPositions()) {
                p.setDepartment(item);
                p.setOrganization(item.getOrganization());
              }
              positions.addAll(item.getPositions());
              return item;
            },
            "children");
    departments = ObjectUtil.flat(departments, "children");

    this.departmentDao.saveAllInBatch(departments);

    departments = ObjectUtil.tree(departments, "id", "parent.id", "children");
    departments =
        ObjectUtil.recursive(
            departments,
            (item, context) -> {
              if (item.getParent() == null) {
                item.setPath(item.getId() + Department.PATH_SEPARATOR);
              } else {
                item.setPath(item.getParent().getPath() + item.getId() + Department.PATH_SEPARATOR);
              }
              return item;
            });
    departments = ObjectUtil.flat(departments, "children");
    this.departmentDao.updateAllInBatch(departments);
    this.positionDao.saveAllInBatch(positions);

    return departments;
  }

  private void saveDepartmentTypes(
      OrganizationDimension dimension, List<DepartmentType> departmentTypes, boolean isNewOrg) {
    for (int i = 0; i < departmentTypes.size(); i++) {
      DepartmentType departmentType = departmentTypes.get(i);
      Optional<DepartmentType> optionalDepartmentType =
          isNewOrg
              ? Optional.empty()
              : this.departmentTypeDao.findOne(
                  PropertyFilter.builder()
                      .equal("code", departmentType.getCode())
                      .equal("dimension.id", dimension.getId())
                      .build());
      if (!optionalDepartmentType.isPresent()) {
        departmentType.setDimension(dimension);
        departmentType.setOrganization(dimension.getOrganization());
        optionalDepartmentType = Optional.of(this.departmentTypeDao.save(departmentType));
      } else {
        DepartmentType oldObj = optionalDepartmentType.get();
        oldObj.setName(departmentType.getName());
        this.departmentTypeDao.update(oldObj);
      }
      departmentTypes.set(i, optionalDepartmentType.get());
    }
  }

  private List<EmployeeStatus> saveEmployeeStatuses(
      OrganizationDimension dimension, List<EmployeeStatus> statuses, boolean isNewOrg) {
    for (int i = 0; i < statuses.size(); i++) {
      EmployeeStatus status = statuses.get(i);
      Optional<EmployeeStatus> optionalStatus =
          isNewOrg
              ? Optional.empty()
              : this.employeeStatusDao.findOne(
                  PropertyFilter.builder()
                      .equal("code", status.getCode())
                      .equal("dimension.id", dimension.getId())
                      .build());
      if (!optionalStatus.isPresent()) {
        status.setDimension(dimension);
        status.setOrganization(dimension.getOrganization());
        optionalStatus = Optional.of(this.employeeStatusDao.save(status));
      } else {
        EmployeeStatus oldObj = optionalStatus.get();
        oldObj.setName(status.getName());
        this.employeeStatusDao.update(oldObj);
      }
      statuses.set(i, optionalStatus.get());
    }
    return statuses;
  }

  public Organization save(Organization organization) {
    organization.setDimensions(new ArrayList<>());
    this.organizationDao.save(organization);

    OrganizationDimension dimension =
        OrganizationDimension.builder()
            .code(Organization.DEFAULT_DIMENSION)
            .name("成员")
            .organization(organization)
            .build();

    this.organizationDimensionDao.save(dimension);

    List<EmployeeStatus> statuses = new ArrayList<>();
    statuses.add(
        EmployeeStatus.builder()
            .organization(organization)
            .dimension(dimension)
            .code(MemberRole.ADMIN.getValue())
            .name("管理员")
            .build());
    statuses.add(
        EmployeeStatus.builder()
            .organization(organization)
            .dimension(dimension)
            .code(MemberRole.USER.getValue())
            .name("成员")
            .build());

    this.employeeStatusDao.saveAll(statuses);

    dimension.setStatuses(statuses);

    organization.getDimensions().add(dimension);

    return organization;
  }

  public Optional<Organization> findByCode(String code) {
    return organizationDao.findOneBy("code", code);
  }

  public Optional<Organization> findById(Long id) {
    return organizationDao.findById(id);
  }

  public Organization getById(Long id) {
    return organizationDao.getReferenceById(id);
  }

  public Organization update(Long id, boolean merge, Organization department) {
    department.setId(id);
    return this.organizationDao.update(department, merge);
  }

  public Organization updateOrganizationProfile(Long id, Organization organization) {
    organization.setId(id);
    Organization oldOrganization = this.organizationDao.getReferenceById(id);
    BeanUtil.copyProperties(
        oldOrganization,
        organization,
        (Property property, Object value, Object _dest) -> {
          if ("logo".equals(property.getName())) {
            return true;
          }
          return value != null;
        });
    return this.organizationDao.update(oldOrganization);
  }

  public Organization renameOrganizationCode(Long id, String code) {
    Organization oldOrganization = this.organizationDao.getReferenceById(id);
    oldOrganization.setCode(code);
    return this.organizationDao.update(oldOrganization);
  }

  public void deleteOrganization(Long id) {
    // 删除成员

    // 删除邀请

    // 删除团队

    this.organizationDao.deleteById(id);
  }

  public List<Organization> findAll(List<PropertyFilter> filters) {
    return this.organizationDao.findAll(
        filters, new OrderBy("updatedAt", OrderBy.Direction.DESC).toSort());
  }

  public Optional<Organization> findOneByCode(String code) {
    return this.organizationDao.findOneBy("code", code);
  }

  public void deleteByCode(String code) {
    Optional<Organization> optionalOrganization = this.organizationDao.findOneBy("code", code);
    if (!optionalOrganization.isPresent()) {
      return;
    }
    Organization organization = optionalOrganization.get();
    this.employeeIdentityDao.deleteByOrgId(organization.getId());
    this.employeeEmailDao.deleteByOrgId(organization.getId());
    this.employeePhoneDao.deleteByOrgId(organization.getId());
    this.employeePositionDao.deleteByOrgId(organization.getId());
    this.employeeDao.deleteByOrgId(organization.getId());
    this.teamMemberDao.deleteByOrgId(organization.getId());
    this.positionDao.deleteByOrgId(organization.getId());
    this.organizationDao.deleteById(optionalOrganization.get().getId());
  }
}
