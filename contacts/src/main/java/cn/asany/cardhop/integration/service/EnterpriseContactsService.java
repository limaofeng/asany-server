package cn.asany.cardhop.integration.service;

import cn.asany.cardhop.contacts.bean.Contact;
import cn.asany.cardhop.contacts.bean.ContactBook;
import cn.asany.cardhop.contacts.bean.ContactGroup;
import cn.asany.cardhop.contacts.bean.enums.ContactBookType;
import cn.asany.cardhop.contacts.graphql.type.ContactGroupNamespace;
import cn.asany.cardhop.integration.IContactsService;
import cn.asany.cardhop.integration.convert.EnterpriseContactsConverter;
import cn.asany.organization.core.bean.Department;
import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.bean.OrganizationDimension;
import cn.asany.organization.core.service.DepartmentService;
import cn.asany.organization.employee.bean.Employee;
import cn.asany.organization.employee.service.EmployeeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Service;

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
    Organization organization = (Organization) book.getOwner();

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
  public Pager<Contact> findPager(
      ContactBook book, String namespace, Pager<Contact> pager, List<PropertyFilter> filters) {
    Organization organization = (Organization) book.getOwner();

    String finalNamespace =
        StringUtil.defaultValue(namespace, () -> ContactGroupNamespace.ENTERPRISE_DEPARTMENT);

    if (ContactGroupNamespace.ENTERPRISE_DEPARTMENT.equals(finalNamespace)) {
      Pager<Employee> _pager = new Pager(pager);
      PropertyFilterBuilder builder = PropertyFilter.builder().and(filters);
      _pager = this.employeeService.findPager(_pager, builder.build());
      List<Contact> contacts = contactsConverter.toContacts(_pager.getPageItems());
      pager = new Pager<>(_pager);
      pager.reset(contacts);
      return pager;
    }

    return pager;
  }

  @Override
  public Optional<Contact> findContactById(Long id) {
    Optional<Employee> optional = this.employeeService.findById(id);
    return optional.map(this.contactsConverter::toContact);
  }
}
