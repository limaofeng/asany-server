package cn.asany.cardhop.integration.service;

import cn.asany.cardhop.contacts.domain.Contact;
import cn.asany.cardhop.contacts.domain.ContactBook;
import cn.asany.cardhop.contacts.domain.ContactGroup;
import cn.asany.cardhop.contacts.domain.enums.ContactBookType;
import cn.asany.cardhop.contacts.graphql.type.ContactGroupNamespace;
import cn.asany.cardhop.integration.IContactsService;
import cn.asany.cardhop.integration.convert.EnterpriseContactsConverter;
import cn.asany.organization.core.domain.Department;
import cn.asany.organization.core.domain.Organization;
import cn.asany.organization.core.domain.OrganizationDimension;
import cn.asany.organization.core.service.DepartmentService;
import cn.asany.organization.employee.domain.Employee;
import cn.asany.organization.employee.service.EmployeeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 企业通讯录
 *
 * @author limaofeng
 */
@Service
public class EnterpriseContactsService implements IContactsService {

  private final DepartmentService departmentService;
  private final EmployeeService employeeService;
  private final EnterpriseContactsConverter contactsConverter;

  public EnterpriseContactsService(
      DepartmentService departmentService,
      EnterpriseContactsConverter contactsConverter,
      EmployeeService employeeService) {
    this.departmentService = departmentService;
    this.contactsConverter = contactsConverter;
    this.employeeService = employeeService;
  }

  @Override
  public ContactBookType getType() {
    return ContactBookType.ENTERPRISE;
  }

  @Override
  public List<ContactGroup> getGroups(ContactBook book, String namespace) {
    Organization organization = null; // (Organization) book.getOwner();

    String finalNamespace =
        StringUtil.defaultValue(namespace, () -> ContactGroupNamespace.ENTERPRISE_DEPARTMENT);

    if (ContactGroupNamespace.ENTERPRISE_DEPARTMENT.equals(finalNamespace)) {
      List<Department> departments =
          this.departmentService.findAllByOrgAndDimension(
              organization.getId(), OrganizationDimension.KEY_OF_DEFAULT);

      List<ContactGroup> groups = contactsConverter.toContactGroups(departments);

      return groups.stream()
          .peek(
              item -> {
                item.setBook(book);
                item.setNamespace(finalNamespace);
              })
          .collect(Collectors.toList());
    }

    return new ArrayList<>();
  }

  @Override
  public Page<Contact> findPage(
      ContactBook book, String namespace, Pageable pageable, PropertyFilter filter) {
    Organization organization = null; // (Organization) book.getOwner();

    String finalNamespace =
        StringUtil.defaultValue(namespace, () -> ContactGroupNamespace.ENTERPRISE_DEPARTMENT);

    if (ContactGroupNamespace.ENTERPRISE_DEPARTMENT.equals(finalNamespace)) {
      Page<Employee> page = this.employeeService.findPage(pageable, filter);
      return page.map(contactsConverter::toContact);
    }

    return Page.empty(pageable);
  }

  @Override
  public Optional<Contact> findContactById(Long id) {
    Optional<Employee> optional = this.employeeService.findById(id);
    return optional.map(this.contactsConverter::toContact);
  }
}
