package cn.asany.organization.employee.service;

import cn.asany.base.common.domain.enums.EmailStatus;
import cn.asany.base.common.domain.enums.PhoneNumberStatus;
import cn.asany.organization.core.dao.DepartmentDao;
import cn.asany.organization.core.dao.EmployeeIdentityDao;
import cn.asany.organization.core.domain.*;
import cn.asany.organization.core.domain.enums.LinkType;
import cn.asany.organization.core.domain.enums.MemberRole;
import cn.asany.organization.core.domain.enums.SocialProvider;
import cn.asany.organization.core.service.OrganizationService;
import cn.asany.organization.employee.dao.*;
import cn.asany.organization.employee.domain.*;
import cn.asany.organization.employee.domain.enums.InviteStatus;
import cn.asany.organization.relationship.dao.EmployeePositionDao;
import cn.asany.organization.relationship.dao.PositionDao;
import cn.asany.organization.relationship.domain.EmployeePosition;
import cn.asany.organization.relationship.domain.Position;
import cn.asany.organization.relationship.service.PositionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.security.crypto.password.PasswordEncoder;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 员工
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class EmployeeService {

  //    @Autowired
  //    private UserDao userDao;
  @Autowired private EmployeeDao employeeDao;
  @Autowired private EmployeePositionDao employeePositionDao;
  @Autowired private PositionDao positionDao;
  @Autowired private EmployeeLinkDao employeeLinkDao;
  @Autowired private EmployeeStatusDao organizationEmployeeStatusDao;
  //    @Autowired
  //    private AddressDao addressDao;
  @Autowired private EmployeeAddressDao employeeAddressDao;
  //    @Autowired
  //    private EmployeeConverter employeeConverter;
  @Autowired private EmployeePhoneDao employeePhoneDao;
  @Autowired private EmployeeEmailDao employeeEmailDao;
  //    @Autowired
  //    private EmailDao emailDao;
  //    @Autowired
  //    private PhoneDao phoneDao;
  @Autowired private DepartmentDao departmentDao;
  //    @Autowired
  //    private UserService userService;
  private PasswordEncoder passwordEncoder;
  @Autowired private PositionService positionService;
  @Autowired private OrganizationService organizationService;
  @Autowired private EmployeeIdentityDao employeeIdentityDao;
  @Autowired private EmployeeStatusDao employeeStatusDao;

  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public Page<Employee> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return employeeDao.findPage(pageable, filters);
  }

  public Employee save(Long orgId, MemberRole role, Employee employee) {
    Organization organization = organizationService.getById(orgId);

    if (employee.getInviteStatus() == null) {
      employee.setInviteStatus(InviteStatus.INACTIVE);
    }

    employee.setOrganization(organization);

    employeeDao.save(employee);

    if (employee.getEmails() != null) {
      for (EmployeeEmail email : employee.getEmails()) {
        if (email.getEmail().getStatus() == null) {
          email.getEmail().setStatus(EmailStatus.UNVERIFIED);
        }
        email.setEmployee(employee);
        this.employeeEmailDao.save(email);
      }
    }

    if (employee.getPhones() != null) {
      for (EmployeePhoneNumber phoneNumber : employee.getPhones()) {
        if (phoneNumber.getPhone().getStatus() == null) {
          phoneNumber.getPhone().setStatus(PhoneNumberStatus.UNVERIFIED);
        }
        phoneNumber.setEmployee(employee);
        this.employeePhoneDao.save(phoneNumber);
      }
    }

    if (employee.getAddresses() != null) {
      for (EmployeeAddress address : employee.getAddresses()) {
        address.setEmployee(employee);
        this.employeeAddressDao.save(address);
      }
    }

    OrganizationDimension dimension =
        ObjectUtil.find(organization.getDimensions(), "code", Organization.DEFAULT_DIMENSION);

    Optional<EmployeeStatus> statusOptional = getStatus(dimension, role.getValue());

    EmployeeStatus employeeStatus = statusOptional.orElseThrow(() -> new NotFoundException("状态为空"));

    EmployeeIdentity identity = EmployeeIdentity.builder().build();
    identity.setEmployee(employee);
    identity.setOrganization(organization);
    identity.setDimension(dimension);
    identity.setStatus(employeeStatus);

    employeeIdentityDao.save(identity);
    return employee;
  }

  private Optional<EmployeeStatus> getStatus(OrganizationDimension dimension, String code) {
    return this.employeeStatusDao.findOne(
        PropertyFilter.builder()
            .equal("dimension.id", dimension.getId())
            .equal("code", code)
            .build());
  }

  //    /**
  //     * 人员账号管理
  //     *
  //     * @param employee
  //     * @param user
  //     * @param organizationId
  //     * @return
  //     * @operation true 新增 false 更新
  //     */
  //    public void employeeUser(Employee employee, User user, String organizationId, boolean
  // operation) {
  //        User exist =
  // this.userDao.findOne(Example.of(User.builder().username(employee.getJobNumber()).build())).orElse(null);
  //        user.setUsername(employee.getJobNumber());
  //        user.setOrganization(Organization.builder().id(organizationId).build());
  //        user.setUserType(UserType.employee);
  //        user.setEmployee(employee);
  //        if (operation) {
  //            if (exist != null) {
  //                throw new NotFoundException("用户名[" + employee.getJobNumber() + "]已经存在");
  //            }
  //            userService.save(user);
  //        } else {
  //            // 更新时账号为空创建账号否则更新
  //            if (exist != null) {
  //                if (!exist.getPassword().equals(user.getPassword())) {
  //                    exist.setPassword(passwordEncoder.encode(user.getPassword()));
  //                }
  //                userDao.update(exist, true);
  //            } else {
  //                userService.save(user);
  //            }
  //        }
  //    }

  //    public Boolean supportDepartmentTypeMultiSectoral(List<EmployeePosition> employeePositions)
  // {
  //        for (EmployeePosition employeePosition : employeePositions) {
  //            // 获取部门类型
  //            Department department = employeePosition.getDepartment();
  //            if (department != null) {
  //                DepartmentType departmentType = employeePosition.getDepartment().getType();
  //                if (departmentType.getMultiSectoral()) {
  //                    // 最大兼岗人数
  //                    Long andPost = departmentType.getAndPost();
  //                    if (employeePositions.size() > andPost) {
  //                        return false;
  //                    }
  //                    continue;
  //                } else {
  //                    if (employeePositions.size() > 1) {
  //                        return false;
  //                    }
  //                    continue;
  //                }
  //            }
  //            continue;
  //        }
  //        return true;
  //    }

  //    public void saveOrganzationEmployees(Employee employee, List<OrganizationEmployee>
  // organizationEmployees) {
  //        for (OrganizationEmployee organizationEmployee : organizationEmployees) {
  //            OrganizationEmployee byEmployeeAndOrganization =
  // this.organizationEmployeeDao.findByEmployeeAndOrganization(employee,
  // organizationEmployee.getOrganization());
  //            //判断是新增还是修改
  //            if (byEmployeeAndOrganization != null) {
  //                if (organizationEmployee.getStatus() != null) {
  //                    OrganizationEmployeeStatus update =
  // organizationEmployeeStatusDao.findByCodeAndOrganization(organizationEmployee.getStatus().getCode(), organizationEmployee.getOrganization());
  //                    byEmployeeAndOrganization.setStatus(update);
  //                    organizationEmployeeDao.update(byEmployeeAndOrganization, true);
  //                    organizationEmployee.setStatus(update);
  //                }
  //
  //                if (CollectionUtils.isNotEmpty(organizationEmployee.getPositions())) {
  //                    organizationEmployee.setId(byEmployeeAndOrganization.getId());
  //                    saveEmployeePositions(employee, organizationEmployee,
  // this.get(employee.getId()).getEmployeePositions());
  //                }
  //            } else {
  //                OrganizationEmployeeStatus status;
  //                //判断是否是默认状态
  //                if (organizationEmployee.getStatus() == null) {
  //                    status = organizationEmployeeStatusDao.findByIsDefaultAndOrganization(true,
  // organizationEmployee.getOrganization());
  //                } else {
  //                    status =
  // organizationEmployeeStatusDao.findByCodeAndOrganization(organizationEmployee.getStatus().getCode(), organizationEmployee.getOrganization());
  //                }
  //                organizationEmployee.setEmployee(employee);
  //                organizationEmployee.setStatus(status);
  //                //保存
  //                organizationEmployee = this.organizationEmployeeDao.save(organizationEmployee);
  //                if (CollectionUtils.isNotEmpty(organizationEmployee.getPositions())) {
  //                    employee.setEmployeePositions(organizationEmployee.getPositions());
  //                    saveEmployeePositions(employee, organizationEmployee,
  // Collections.emptyList());
  //                }
  //            }
  //        }
  //        employee.setOrganizationEmployees(organizationEmployees);
  //    }

  //    public Employee save(Employee employee) {
  //        return this.save(employee, Collections.emptyList(),
  //            Collections.emptyList(), Collections.emptyList(),
  //            Collections.emptyList(), User.builder().build());
  //    }

  @Transactional
  public Employee get(Long id) {
    return employeeDao.getReferenceById(id);
  }

  public Employee update(Long id, boolean merge, Employee employee) {
    employee.setId(id);
    Employee old = this.get(id);
    //        if (CollectionUtils.isNotEmpty(organizationEmployees)) {
    //            saveOrganzationEmployees(employee, organizationEmployees);
    //        } else {
    //            organizationEmployeeService.saveOrganzationEmployees(employee,
    // old.getEmployeePositions());
    //        }
    //        if (CollectionUtils.isNotEmpty(fieldValues)) {
    //            for (FieldValue fieldValue : fieldValues) {
    //                String groupIdAndValue = fieldValue.getName();
    //                //切割前端穿的GroupID.name
    //                String[] strings = groupIdAndValue.split("_");
    //                Long groupId = Long.valueOf(strings[0]);
    //                //查询员工自定义字段
    //                EmployeeField employeeField =
    // employeeFieldDao.findOne(PropertyFilter.builder().equal("employeeFieldGroup.id",
    // groupId).equal("name", strings[1]).build()).orElse(null);
    //                //查询自定义字段值
    //                List<EmployeeFieldValue> employeeFieldValues =
    // employeeFieldValueDao.findByEmployeeFieldAndEmployee(employeeField, employee);
    //                if (employeeField != null) {
    //                    if (employeeFieldValues != null && employeeFieldValues.size() > 0) {
    //                        EmployeeFieldValue employeeFieldValue = EmployeeFieldValue.builder()
    //                            .id(employeeFieldValues.get(0).getId())
    //                            .value(fieldValue.getValue())
    //                            .employee(employee)
    //                            .build();
    //                        employeeFieldValueDao.update(employeeFieldValue, merge);
    //                    } else {
    //                        EmployeeFieldValue employeeFieldValue = EmployeeFieldValue.builder()
    //                            .value(fieldValue.getValue())
    //                            .employee(employee)
    //                            .employeeField(employeeField)
    //                            .build();
    //                        employeeFieldValueDao.save(employeeFieldValue);
    //                    }
    //                }
    //            }
    //        }
    //        if (employee.getAutographPngs() != null) {
    //            for (AutographPng auto : employee.getAutographPngs()) {
    //                this.autographPngDao.save(auto);
    //            }
    //        }
    //        Employee employeeReturn = this.employeeDao.update(employee, merge);
    //        if (StringUtil.isNotBlank(user.getPassword())) {
    //            employeeUser(employee, user,
    // organizationEmployees.get(0).getOrganization().getId(), false);
    //        }
    //        //员工地址
    //        if (CollectionUtils.isNotEmpty(employeeAddressList)) {
    //            //员工所有的地址
    //            List<EmployeeAddress> employeeList = employeeReturn.getAddresses();
    //            List<EmployeeAddress> employeeSaveList = new ArrayList<>();
    //            //数据库有而我传过来的没有 删除
    //            List<EmployeeAddress> deleteList = employeeList.stream().filter(item ->
    // !employeeAddressList.stream().map(e ->
    // e.getLabel()).collect(Collectors.toList()).contains(item.getLabel())).collect(Collectors.toList());
    //            employeeAddressDao.deleteAll(deleteList);
    //            for (EmployeeAddress addressValue : employeeAddressList) {
    //                List<EmployeeAddress> employeeAddress = employeeList.stream().filter(a ->
    // a.getLabel().equals(addressValue.getLabel())).collect(Collectors.toList());
    //                Address address = addressValue.getAddress();
    //                if (CollectionUtils.isEmpty(employeeAddress)) { //新增
    //
    // employeeSaveList.add(EmployeeAddress.builder().address(address).label(addressValue.getLabel()).employee(employee).primary(addressValue.getPrimary()).build());
    //                } else {//修改
    //                    for (EmployeeAddress employeeAddr : employeeAddress) {
    //                        address.setId(employeeAddr.getAddress().getId());
    //                        addressDao.update(address, true);
    //                    }
    //                }
    //
    //                if (CollectionUtils.isNotEmpty(employeeSaveList)) {
    //                    employeeAddressDao.saveAll(employeeSaveList);
    //                }
    //            }
    //        }
    //        //修改员工电话
    //        if (CollectionUtils.isNotEmpty(employeePhoneList)) {
    //            //员工所有的电话
    //            List<EmployeePhone> employeeList = employeeReturn.getPhones();
    //            List<EmployeePhone> employeeSaveList = new ArrayList<>();
    //            //数据库有而我传过来的没有 删除
    //            List<EmployeePhone> deleteList = employeeList.stream().filter(item ->
    // !employeePhoneList.stream().map(e ->
    // e.getLabel()).collect(Collectors.toList()).contains(item.getLabel())).collect(Collectors.toList());
    //            employeePhoneDao.deleteAll(deleteList);
    //            for (EmployeePhone phoneValue : employeePhoneList) {
    //                List<EmployeePhone> employeePhones = employeeList.stream().filter(a ->
    // a.getLabel().equals(phoneValue.getLabel())).collect(Collectors.toList());
    //                Phone phone = phoneValue.getPhone();
    //                phone.setStatus(PhoneStatus.Unverified);
    //                if (CollectionUtils.isEmpty(employeePhones)) { //新增
    //
    // employeeSaveList.add(EmployeePhone.builder().phone(phone).label(phoneValue.getLabel()).employee(employee).primary(phoneValue.getPrimary()).build());
    //                    if (phoneValue.getLabel().equals(FIELD_MOBILE) && phoneValue.getPrimary())
    // {
    //                        employeeReturn.setMobile(phone.getPhone());
    //                        employeeReturn = this.employeeDao.update(employeeReturn, merge);
    //                    }
    //                } else {//修改
    //                    for (EmployeePhone empPhone : employeePhones) {
    //                        if (empPhone.getLabel().equals(FIELD_MOBILE) && empPhone.getPrimary())
    // {
    //                            employeeReturn.setMobile(phoneValue.getPhone().getPhone());
    //                            employeeReturn = this.employeeDao.update(employeeReturn, merge);
    //                        }
    //                        phone.setId(empPhone.getPhone().getId());
    //                        phoneDao.update(phone, true);
    //                    }
    //                }
    //
    //                if (CollectionUtils.isNotEmpty(employeeSaveList)) {
    //                    employeePhoneDao.saveAll(employeeSaveList);
    //                }
    //            }
    //        }
    //        //邮箱
    //        if (CollectionUtils.isNotEmpty(employeeEmailList)) {
    //            List<EmployeeEmail> emails = employeeReturn.getEmails();
    //            List<EmployeeEmail> employeeSaveEmailList = new ArrayList<>();
    //            List<EmployeeEmail> deleteList = emails.stream().filter(item ->
    // !employeeEmailList.stream().map(e ->
    // e.getLabel()).collect(Collectors.toList()).contains(item.getLabel())).collect(Collectors.toList());
    //            employeeEmailDao.deleteAll(deleteList);
    //            for (EmployeeEmail employeeEmail : employeeEmailList) {
    //                List<EmployeeEmail> employeeEmails = emails.stream().filter(a ->
    // a.getLabel().equals(employeeEmail.getLabel())).collect(Collectors.toList());
    //                Email email = employeeEmail.getEmail();
    //                if (CollectionUtils.isEmpty(employeeEmails)) { //新增
    //
    // employeeSaveEmailList.add(EmployeeEmail.builder().email(email).label(employeeEmail.getLabel()).employee(employee).primary(employeeEmail.getPrimary()).build());
    //                } else {//修改
    //                    for (EmployeeEmail employeeEmail1 : employeeEmails) {
    //                        email.setId(employeeEmail1.getEmail().getId());
    //                        emailDao.update(email, true);
    //                    }
    //                }
    //
    //                if (CollectionUtils.isNotEmpty(employeeSaveEmailList)) {
    //                    employeeEmailDao.saveAll(employeeSaveEmailList);
    //                }
    //            }
    //        }
    return employee;
  }

  //    public Employee update(Long id, boolean merge, Employee employee) {
  //        return this.update(id, merge, employee, null, null, null, null, null, null);
  //    }

  //    public void saveEmployeePositions(Employee employee, OrganizationEmployee
  // organizationEmployee, List<EmployeePosition> oldEmployeePositions) {
  //        Organization organization = organizationEmployee.getOrganization();
  //        // 过滤当前维护组织的职位
  //        oldEmployeePositions = oldEmployeePositions.stream().filter(ep ->
  // ep.getOrganization().getId().equals(organization.getId())).collect(Collectors.toList());
  //        OrganizationEmployee updatePrimaryPosition = new OrganizationEmployee();
  //        for (EmployeePosition employeePosition : organizationEmployee.getPositions()) {
  //            if (employeePosition.getPosition() == null) {
  //                continue;
  //            }
  //
  //            Position position = positionDao.getOne(employeePosition.getPosition().getId());
  //            EmployeePosition oldEmployeePosition = ObjectUtil.remove(oldEmployeePositions,
  // "position.id", employeePosition.getPosition().getId());
  //            employeePosition.setDepartment(position.getDepartment());
  //            employeePosition.setStatus(organizationEmployee);
  //            employeePosition.setPosition(position);
  //            employeePosition.setEmployee(employee);
  //            employeePosition.setOrganization(organizationEmployee.getOrganization());
  //            EmployeePosition save;
  //            if (oldEmployeePosition == null) {
  //                save = this.employeePositionDao.save(employeePosition);
  //            } else {
  //                // TODO 除了 Department / Employee / Organization / 还会修改什么 ？
  //                oldEmployeePosition.setPrimary(employeePosition.getPrimary());
  //                save = this.employeePositionDao.save(oldEmployeePosition);
  //            }
  //            //主部门则修改组织人员表中部门和职务
  //            if (employeePosition.getPrimary()) {
  //                organizationEmployee.setDepartment(employeePosition.getDepartment());
  //                organizationEmployee.setPosition(employeePosition.getPosition());
  //                updatePrimaryPosition = organizationEmployee;
  //            }
  //        }
  //        if (ObjectUtils.isNotEmpty(updatePrimaryPosition)) {
  //            organizationEmployeeDao.save(updatePrimaryPosition);
  //        }
  //        for (EmployeePosition department : oldEmployeePositions) {
  //            this.employeePositionDao.delete(department);
  //        }
  //    }

  public void delete(Long id) {
    //        List<EmployeeFieldValue> fieldValues =
    // this.employeeFieldValueDao.findByEmployee(Employee.builder().id(id).build());
    //        this.employeeFieldValueDao.deleteAll(fieldValues);
    this.employeeDao.deleteById(id);
  }

  public List<Employee> findAllByOrg(String orgId) {
    return this.employeeDao.findAll(
        (Specification<Employee>)
            (root, query, builder) ->
                builder.equal(
                    root.join("employeePositions").join("organization").get("id"), orgId));
  }

  public List<Employee> findAll(List<PropertyFilter> filters) {
    return this.employeeDao.findAll(filters);
  }

  public Employee findOneBySN(String sn) {
    return this.employeeDao
        .findOne(Example.of(Employee.builder().jobNumber(sn).build()))
        .orElse(null);
  }

  public void batchUpdateEmployeePosition(List<Long> ids, Long positionId) {
    //        Position position = positionDao.getOne(positionId);
    //        List<Employee> employees = this.employeeDao.findAll((Specification<Employee>) (root,
    // query, criteriaBuilder) -> root.get("id").in(ids));
    //
    //        OrganizationEmployeeStatus byIsDefaultAndOrganization =
    // organizationEmployeeStatusDao.findByIsDefaultAndOrganization(true,
    // position.getOrganization());
    //        for (Employee employee : employees) {
    //            if (!ObjectUtil.exists(employee.getEmployeePositions(), "position.id",
    // position.getId())) {
    //                OrganizationEmployee organizationEmployee =
    // organizationEmployeeDao.findByEmployeeAndOrganization(employee, position.getOrganization());
    //                if (organizationEmployee == null) {
    //                    organizationEmployee =
    // organizationEmployeeDao.save(OrganizationEmployee.builder().organization(position.getOrganization()).position(position).employee(employee).status(byIsDefaultAndOrganization).build());
    //                }
    //                employeePositionDao.save(EmployeePosition.builder()
    //                    .employee(employee)
    //                    .department(position.getDepartment())
    //                    .position(position)
    //                    .organization(position.getOrganization())
    //                    .status(organizationEmployee)
    //                    .primary(false).build());
    //            }
    //        }
  }

  public Optional<Employee> getByLink(LinkType type, String link) {
    return this.employeeDao.findOne(
        (Specification<Employee>)
            (root, query, builder) -> {
              Join join = root.join("links");
              return builder.and(
                  builder.equal(join.get("type"), type), builder.equal(join.get("linkId"), link));
            });
  }

  public void addEmployeeLink(LinkType type, Long employee, String linkId) {
    //        Optional<EmployeeLink> optional =
    // this.employeeLinkDao.findOne(Example.of(EmployeeLink.builder()
    //            .employee(Employee.builder().id(employee).build())
    //            .type(type)
    //            .build()));
    //        if (optional.isPresent()) {
    //            EmployeeLink link = optional.get();
    //            link.setLinkId(linkId);
    //            this.employeeLinkDao.update(link, false);
    //        } else {
    //            EmployeeLink link = EmployeeLink.builder()
    //                .type(type)
    //                .employee(Employee.builder().id(employee).build())
    //                .linkId(linkId)
    //                .build();
    //            this.employeeLinkDao.save(link);
    //        }
  }

  public List<Employee> findAllByDepartment(Long id) {
    PropertyFilterBuilder builder = PropertyFilter.builder();
    builder.equal("employeePositions.department.id", id);
    return this.employeeDao.findAll(builder.build());
  }

  public Long totalEmployeeNumber(Department department) {
    return employeePositionDao.count(
        Example.of(
            EmployeePosition.builder()
                .department(department)
                .organization(department.getOrganization())
                .build()));
  }

  public EmployeePosition findPositionOne(
      Long employeeId, Long originalDeparmentId, Long organizationId) {
    List<EmployeePosition> list =
        employeePositionDao.findAll(
            Example.of(
                EmployeePosition.builder()
                    .employee(Employee.builder().id(employeeId).build())
                    .department(Department.builder().id(originalDeparmentId).build())
                    .organization(Organization.builder().id(organizationId).build())
                    .build()));
    if (list.size() > 1) {
      for (EmployeePosition employeePosition : list) {
        if (employeePosition.getPrimary() != null && employeePosition.getPrimary()) {
          return employeePosition;
        }
      }
    } else if (list.size() == 1) {
      return list.get(0);
    }
    return null;
  }

  public EmployeePosition updateEmployeePosition(EmployeePosition build) {
    EmployeePosition all =
        this.findPositionOne(
            build.getEmployee().getId(),
            build.getDepartment().getId(),
            build.getOrganization().getId());
    if (all != null) {
      return all;
    } else {
      if (build.getPrimary()) {
        //                OrganizationEmployee organizationEmployee =
        // organizationEmployeeService.get(build.getOrganization().getId(),
        // build.getEmployee().getId()).orElse(null);
        //                if (organizationEmployee != null) {
        //                    organizationEmployee.setDepartment(build.getDepartment());
        //                    organizationEmployee.setPosition(build.getPosition());
        //                    organizationEmployeeDao.update(organizationEmployee, true);
        //
        //                }
      }
      return employeePositionDao.update(build, true);
    }
  }

  //    public List<Employee> findEmployeeAllByDep(Long id, String status) {
  //        return
  // employeePositionDao.findByDepartment(Department.builder().id(id).build()).stream().filter(employeePosition ->
  //
  // employeePosition.getStatus().getStatus().getCode().equals(status)).collect(Collectors.toList())
  //            .stream().map(EmployeePosition::getEmployee).collect(Collectors.toList());
  //    }

  //    public EmployeeByDepAndYearReport findEmployeeByDepAndYear(long departmentId, String year) {
  //        year = year + "/01/01 00:00:00";
  //        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd mm:hh:ss");
  //        Date date = new Date();
  //        try {
  //            date = simpleDateFormat.parse(year);
  //        } catch (ParseException e) {
  //            log.error(e.getMessage(), e);
  //        }
  //        Date finalDate = date;
  //        int size =
  // employeePositionDao.findByDepartment(Department.builder().id(departmentId).build()).stream().filter(employeePosition ->
  //
  // employeePosition.getUpdatedAt().before(finalDate)).collect(Collectors.toList()).size();
  //        Date now = new Date();
  //        PropertyFilterBuilder builder = new PropertyFilterBuilder();
  //        builder.between("updatedAt", finalDate, now);
  //        builder.equal("department", departmentId);
  //        List<DepartmentChangeRecord> all = departmentChangeRecordDao.findAll(builder.build());
  //        return EmployeeByDepAndYearReport.builder().size(size).result(all).build();
  //    }

  public Optional<EmployeePosition> findOneByPositionNameAndDep(
      String name, Department department) {
    return employeePositionDao.findOne(
        Example.of(
            EmployeePosition.builder()
                .position(Position.builder().name(name).build())
                .department(department)
                .build()));
  }

  public List<Employee> findByIds(List<Long> ids) {
    return employeeDao.findAllById(ids);
  }

  public Optional<Employee> findById(Long id) {
    return this.employeeDao.findById(id);
  }

  public List<Employee> findAllByDepartments(List<Long> ids) {
    List<Department> departments = departmentDao.findAllById(ids);
    List<Employee> employees = new ArrayList<>();
    for (Department department : departments) {
      List<EmployeePosition> employeePositions = department.getEmployees();
      for (EmployeePosition employeePosition : employeePositions) {
        employees.add(employeePosition.getEmployee());
      }
    }
    return employees;
  }

  public Long findEmployeeCount(List<PropertyFilter> filters) {
    return employeeDao.count(filters);
  }

  public Long findEmployeeCount(Department department, List<String> status) {
    PropertyFilterBuilder builder = new PropertyFilterBuilder();
    // 根据部门path路径和人员状态过滤
    builder.startsWith("employeePositions.department.path", department.getPath());
    if (status != null) {
      for (String statu : status) {
        builder.equal("employeePositions.status.status.code", statu);
      }
    }
    return employeeDao.count(builder.build());
  }

  //    public Optional<OrganizationEmployee> getOrganizationEmployee(Employee employee) {
  //        if (employee.getCurrentOrganization() == null) {
  //            return Optional.empty();
  //        }
  //        return organizationEmployeeService.get(employee.getCurrentOrganization().getId(),
  // employee.getId());
  //    }

  public void batchDelete(List<Long> ids) {
    List<Employee> employeeList =
        ids.stream()
            .map(
                id -> {
                  return employeeDao.getOne(id);
                })
            .collect(Collectors.toList());
    this.employeeDao.deleteAll(employeeList);
  }

  //    public String getDeptsNodesNames(String orgId, Employee employee) {
  //        List<Long> deptIds = employee.getDepartmentIds();
  //        if (CollectionUtils.isEmpty(deptIds)) {
  //            return "";
  //        }
  //        List<PropertyFilter> filters = new DepartmentFilter().getBuilder()
  //            .equal("organization.id", orgId)
  //            .in("id", deptIds)
  //            .build();
  //        List<Department> departments = departmentDao.findAll(filters);
  //        List<String> deptNamesNodesList = departments.stream().map(dept ->
  // getDeptNodesNames(dept)).collect(Collectors.toList());
  //        if (CollectionUtils.isEmpty(deptNamesNodesList)) {
  //            return "";
  //        }
  //        return String.join(",", deptNamesNodesList);
  //    }

  private String getDeptNodesNames(Department dept) {
    String[] paths = dept.getPath().split("/");
    StringBuilder sb = new StringBuilder();
    int index = 0;
    for (String path : paths) {
      if (!"".equals(path)) {
        Department department = departmentDao.findById(Long.valueOf(path)).orElse(null);
        if (department != null) {
          sb.append(index++ == 0 ? "" : ".").append(department.getName());
        }
      }
    }
    return sb.toString();
  }

  //    public Employee createEmployee(EmployeeInput input) {
  //        List<EmployeeFieldValueInput> values = new ArrayList<>();
  //        Employee employee = new Employee();
  //        BeanUtils.copyProperties(input, employee, "departmentIds");
  //        List<OrganizationEmployee> organizationEmployees = new ArrayList<>();
  //        if (CollectionUtils.isNotEmpty((input.getOrganizations()))) {
  //            organizationEmployees = input.getOrganizations().stream().map(item -> {
  //                OrganizationEmployee organizationEmployee = new OrganizationEmployee();
  //                organizationEmployee.setPositions(getEmployeePosition(item.getPositions()));
  //
  // organizationEmployee.setOrganization(getEmployeeOrganizatio(item.getOrganization()));
  //                organizationEmployee.setStatus(item.getStatus());
  //                return organizationEmployee;
  //            }).collect(Collectors.toList());
  //        }
  //        List<FieldValue> fieldValue = new ArrayList<>();
  //        if (CollectionUtils.isNotEmpty(input.getValues())) {
  //            fieldValue = input.getValues().stream().map(item -> {
  //                FieldValue vo = new FieldValue();
  //                vo.setName(item.getName());
  //                vo.setValue(item.getValue());
  //                return vo;
  //            }).collect(Collectors.toList());
  //        }
  //        //电话
  //        List<EmployeePhone> phoneList = new ArrayList<>();
  //        if (CollectionUtils.isNotEmpty(input.getPhones())) {
  //            phoneList = input.getPhones().stream().map(item -> {
  //                if (item.getStatus() == null) {
  //                    item.setStatus(PhoneStatus.Unverified);
  //                }
  //                EmployeePhone phone = employeeConverter.toEmployeePhone(item);
  //
  //                return phone;
  //            }).collect(Collectors.toList());
  //        }
  //        List<EmployeeEmail> emailList = new ArrayList<>();
  //        if (null != input.getEmails() && CollectionUtils.isNotEmpty(input.getEmails())) {
  //            emailList = input.getEmails().stream().map(item -> {
  //                EmployeeEmail employeeEmail = new EmployeeEmail();
  //                employeeEmail.setLabel(item.getLabel());
  //
  // employeeEmail.setEmail(Email.builder().email(item.getEmail()).status(item.getStatus() == null ?
  // EmailStatus.Unverified : item.getStatus()).build());
  //                employeeEmail.setPrimary(item.getPrimary() == null ? true : item.getPrimary());
  //                return employeeEmail;
  //            }).collect(Collectors.toList());
  //        }
  //        return this.save(employee, organizationEmployees, fieldValue, emailList, phoneList,
  // User.builder().password(input.getPassword()).build());
  //    }

  private List<EmployeePosition> getEmployeePosition(List<EmployeePosition> positions) {
    positions.stream()
        .forEach(
            position -> {
              position.setDepartment(getDepartment(position));
            });
    return positions;
  }

  private Department getDepartment(EmployeePosition positions) {
    Department department = new Department();
    if (ObjectUtils.isNotEmpty(positions.getPosition())) {
      Department depar = positionService.get(positions.getPosition().getId()).getDepartment();
      depar.getType().setOrganization(null);
      department.setType(depar.getType());
      department.setId(depar.getId());
      department.setName(depar.getName());
    }
    return department;
  }

  //    public Long getTouristJobNumber() {
  //        long touristId = this.userDao.getTouristId();
  //        this.userDao.updateTouristId();
  //        return touristId;
  //    }

  //    public EmployeeEmail saveEmail(EmployeeEmail employeeEmail) {
  //        return employeeEmailDao.save(employeeEmail);
  //    }

  //    public EmployeeEmail updateEmail(EmployeeEmail employeeEmail, Long emailId, Boolean merge,
  // Email email) {
  //        Optional<EmployeeEmail> employeeEmailOne = employeeEmailDao.findById(emailId);
  //        if (employeeEmailOne.isPresent()) {
  //            Long id = employeeEmailOne.get().getEmail().getId();
  //            email.setId(id);
  //            Email update = emailDao.update(email, merge);
  //            employeeEmail.setEmail(update);
  //            employeeEmail.setId(emailId);
  //            return employeeEmailDao.update(employeeEmail, merge);
  //        }
  //        return null;
  //    }

  //    public Boolean deleteEmail(Long id) {
  //        Optional<EmployeeEmail> byId = employeeEmailDao.findById(id);
  //        if (byId.isPresent()) {
  //            Email email = byId.get().getEmail();
  //            employeeEmailDao.delete(EmployeeEmail.builder().id(id).build());
  //            emailDao.delete(email);
  //            return true;
  //        }
  //        return false;
  //    }

  //    public EmployeePhone savePhone(EmployeePhone employeePhone) {
  //        return employeePhoneDao.save(employeePhone);
  //    }

  //    public EmployeePhone updatePhone(EmployeePhone employeePhone, Long phoneId, Boolean merge,
  // Phone phone) {
  //        Optional<EmployeePhone> employeePhoneOne = employeePhoneDao.findById(phoneId);
  //        if (employeePhoneOne.isPresent()) {
  //            Long id = employeePhoneOne.get().getPhone().getId();
  //            phone.setId(id);
  //            Phone update = phoneDao.update(phone, merge);
  //            employeePhone.setPhone(update);
  //            employeePhone.setId(phoneId);
  //            return employeePhoneDao.update(employeePhone, merge);
  //        }
  //        return null;
  //    }

  //    public Boolean deletePhone(Long id) {
  //        Optional<EmployeePhone> byId = employeePhoneDao.findById(id);
  //        if (byId.isPresent()) {
  //            Phone phone = byId.get().getPhone();
  //            employeePhoneDao.delete(EmployeePhone.builder().id(id).build());
  //            phoneDao.delete(phone);
  //            return true;
  //        }
  //        return false;
  //    }

  /**
   * 账号验证接口
   *
   * @param account
   * @return
   */
  public Boolean accountVerification(String account) {
    //        Optional<User> one =
    // userDao.findOne(Example.of(User.builder().username(account).build()));
    //        if (one.isPresent()) {
    //            if (one.get() != null) {
    //                return true;
    //            }
    //            return false;
    //        }
    return false;
  }

  //    public void createOrUpdateEmployee(int type, List<EmployeeImportInput> employeeImportInputs)
  // {
  //
  //        for (int i = 0; i < employeeImportInputs.size(); i++) {
  //            EmployeeInput employeeInput = new EmployeeInput();
  //            employeeInput.setJobNumber(employeeImportInputs.get(i).getJobNumber());
  //            employeeInput.setSex(employeeImportInputs.get(i).getSex());
  //            employeeInput.setBirthday(employeeImportInputs.get(i).getBirthday());
  //            //邮箱
  //            if (StringUtils.isNotEmpty(employeeImportInputs.get(i).getEmail())) {
  //                EmployeeEmailInput employeeEmailInput = new EmployeeEmailInput();
  //                employeeEmailInput.setLabel("employeeEmail");
  //                employeeEmailInput.setPrimary(true);
  //                employeeEmailInput.setStatus(EmailStatus.Unverified);
  //                employeeEmailInput.setEmail(employeeImportInputs.get(i).getEmail());
  //                List<EmployeeEmailInput> employeeEmailInputList = new ArrayList<>();
  //                employeeEmailInputList.add(employeeEmailInput);
  //                employeeInput.setEmails(employeeEmailInputList);
  //            }
  //            employeeInput.setName(employeeImportInputs.get(i).getName());
  //            employeeInput.setPassword("whir123!");
  //
  //            //手机号码部分
  //            List<EmployeePhoneInput> employeePhoneInputArrayList = new ArrayList<>();
  //            if (StringUtils.isNotEmpty(employeeImportInputs.get(i).getMobile())) {
  //                EmployeePhoneInput employeePhoneInput = new EmployeePhoneInput();
  //                employeePhoneInput.setLabel("mobile");
  //                employeePhoneInput.setPrimary(true);
  //                employeePhoneInput.setPhone(employeeImportInputs.get(i).getMobile());
  //                employeePhoneInputArrayList.add(employeePhoneInput);
  //            }
  //
  //            if (StringUtils.isNotEmpty(employeeImportInputs.get(i).getTel())) {
  //                EmployeePhoneInput employeePhoneInput = new EmployeePhoneInput();
  //                employeePhoneInput.setLabel("tel");
  //                employeePhoneInput.setPrimary(true);
  //                employeePhoneInput.setPhone(employeeImportInputs.get(i).getTel());
  //                employeePhoneInputArrayList.add(employeePhoneInput);
  //            }
  //            employeeInput.setPhones(employeePhoneInputArrayList);
  //
  //            //组织部分
  //            List<OrganizationEmployee> organizationEmployeeList = new ArrayList<>();
  //            OrganizationEmployee organizationEmployee = new OrganizationEmployee();
  //
  // organizationEmployee.setOrganization(Organization.builder().id(employeeImportInputs.get(i).getOrgId()).build());
  //
  // organizationEmployee.setStatus(OrganizationEmployeeStatus.builder().id(employeeImportInputs.get(i).getStatus()).code(employeeImportInputs.get(i).getCode()).build());
  //            EmployeePosition employeePosition = new EmployeePosition();
  //            employeePosition.setPrimary(true);
  //
  // employeePosition.setPosition(Position.builder().id(Long.valueOf(employeeImportInputs.get(i).getPosition())).build());
  //            List<EmployeePosition> employeePositionList = new ArrayList<>();
  //            employeePositionList.add(employeePosition);
  //            organizationEmployee.setPositions(employeePositionList);
  //            organizationEmployeeList.add(organizationEmployee);
  //            employeeInput.setOrganizations(organizationEmployeeList);
  //
  //            //其他自定义部分
  //            List<EmployeeFieldValueInput> employeeFieldValueInputList = new ArrayList<>();
  //            if (StringUtils.isNotBlank(employeeImportInputs.get(i).getEducation())) {
  //                EmployeeFieldValueInput employeeFieldValueInputEducation = new
  // EmployeeFieldValueInput();
  //                employeeFieldValueInputEducation.setName("2_education");
  //
  // employeeFieldValueInputEducation.setValue(employeeImportInputs.get(i).getEducation());
  //                employeeFieldValueInputList.add(employeeFieldValueInputEducation);
  //            }
  //
  //            if (StringUtils.isNotBlank(employeeImportInputs.get(i).getAcademicDegree())) {
  //                EmployeeFieldValueInput employeeFieldValueInputAcademicDegree = new
  // EmployeeFieldValueInput();
  //                employeeFieldValueInputAcademicDegree.setName("2_academicDegree");
  //
  // employeeFieldValueInputAcademicDegree.setValue(employeeImportInputs.get(i).getAcademicDegree());
  //                employeeFieldValueInputList.add(employeeFieldValueInputAcademicDegree);
  //            }
  //
  //            if (StringUtils.isNotBlank(employeeImportInputs.get(i).getIdNumber())) {
  //                EmployeeFieldValueInput employeeFieldValueInputIdNumber = new
  // EmployeeFieldValueInput();
  //                employeeFieldValueInputIdNumber.setName("3_idNumber");
  //
  // employeeFieldValueInputIdNumber.setValue(employeeImportInputs.get(i).getIdNumber());
  //                employeeFieldValueInputList.add(employeeFieldValueInputIdNumber);
  //
  //            }
  //
  //            if (StringUtils.isNotBlank(employeeImportInputs.get(i).getNation())) {
  //                EmployeeFieldValueInput employeeFieldValueInputNation = new
  // EmployeeFieldValueInput();
  //                employeeFieldValueInputNation.setName("3_nation");
  //                employeeFieldValueInputNation.setValue(employeeImportInputs.get(i).getNation());
  //                employeeFieldValueInputList.add(employeeFieldValueInputNation);
  //
  //            }
  //
  //            if (StringUtils.isNotBlank(employeeImportInputs.get(i).getJoinParty())) {
  //                EmployeeFieldValueInput employeeFieldValueInputJionParty = new
  // EmployeeFieldValueInput();
  //                employeeFieldValueInputJionParty.setName("3_inPartyDate");
  //
  // employeeFieldValueInputJionParty.setValue(employeeImportInputs.get(i).getJoinParty());
  //                employeeFieldValueInputList.add(employeeFieldValueInputJionParty);
  //
  //            }
  //
  //            if (StringUtils.isNotBlank(employeeImportInputs.get(i).getKeshi())) {
  //                EmployeeFieldValueInput employeeFieldValueInputKeshi = new
  // EmployeeFieldValueInput();
  //                employeeFieldValueInputKeshi.setName("6_department");
  //                employeeFieldValueInputKeshi.setValue(employeeImportInputs.get(i).getKeshi());
  //                employeeFieldValueInputList.add(employeeFieldValueInputKeshi);
  //
  //            }
  //
  //            if (StringUtils.isNotBlank(employeeImportInputs.get(i).getTitle())) {
  //                EmployeeFieldValueInput employeeFieldValueInputTitle = new
  // EmployeeFieldValueInput();
  //                employeeFieldValueInputTitle.setName("7_title");
  //                employeeFieldValueInputTitle.setValue(employeeImportInputs.get(i).getTitle());
  //                employeeFieldValueInputList.add(employeeFieldValueInputTitle);
  //            }
  //
  //            if (!employeeFieldValueInputList.isEmpty()) {
  //                employeeInput.setValues(employeeFieldValueInputList);
  //            }
  //            if (type == 0) {
  //                //新增
  //                createEmployee(employeeInput);
  //            } else {
  //                //修改的情况
  //            }
  //        }
  //    }

  /**
   * 获取当前生日的用户ID
   *
   * @param birth MMDD
   * @return
   */
  public List<Long> getBirthEmpIds(String birth) {
    return new ArrayList<>(); // employeeDao.findBirthEmployeeIds(birth);
  }

  public Optional<Employee> findOneByMobile(String mobile) {
    PropertyFilterBuilder builder = PropertyFilter.builder();
    builder.equal("mobile", mobile);
    List<Employee> employees = this.employeeDao.findAll(builder.build());
    if (employees.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(employees.get(0));
  }

  public Optional<Employee> findOneByLink(LinkType dingtalk, String id) {
    PropertyFilterBuilder builder = PropertyFilter.builder();
    builder.equal("links.type", dingtalk);
    builder.equal("links.linkId", id);
    List<Employee> employees = this.employeeDao.findAll(builder.build());
    if (employees.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(employees.get(0));
  }

  public Employee connected(Employee employee, SocialProvider provider, String id) {
    if (provider != SocialProvider.dingtalk) {
      throw new ValidationException("1002004", "不支持 provider = " + provider + " 绑定失败");
    }
    PropertyFilterBuilder builder = PropertyFilter.builder();
    builder.equal("employee.id", employee.getId());
    builder.equal("type", LinkType.valueOf(provider.name()));
    Optional<EmployeeLink> optional = employeeLinkDao.findOne(builder.build());
    if (optional.isPresent()) {
      EmployeeLink link = optional.get();
      link.setLinkId(id);
      employeeLinkDao.update(link);
    } else {
      employeeLinkDao.save(
          EmployeeLink.builder()
              .employee(employee)
              .type(LinkType.valueOf(provider.name()))
              .linkId(id)
              .build());
    }
    return employee;
  }

  public void disconnect(Employee employee, SocialProvider provider) {
    PropertyFilterBuilder builder = PropertyFilter.builder();
    builder.equal("employee.id", employee.getId());
    builder.equal("type", LinkType.valueOf(provider.name()));
    Optional<EmployeeLink> optional = employeeLinkDao.findOne(builder.build());
    if (!optional.isPresent()) {
      return;
    }
    employeeLinkDao.delete(optional.get());
  }

  public Optional<Employee> findOneByUser(Long uid) {
    return this.employeeDao.findOneBy("user.id", uid);
  }
}
