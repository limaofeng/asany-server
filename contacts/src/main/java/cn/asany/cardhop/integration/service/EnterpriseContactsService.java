package cn.asany.cardhop.integration.service;

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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Service;

@Service
public class EnterpriseContactsService implements IContactsService {

  private final DepartmentService departmentService;
  private final EnterpriseContactsConverter contactsConverter;

  public EnterpriseContactsService(
      DepartmentService departmentService, EnterpriseContactsConverter contactsConverter) {
    this.departmentService = departmentService;
    this.contactsConverter = contactsConverter;
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
}
