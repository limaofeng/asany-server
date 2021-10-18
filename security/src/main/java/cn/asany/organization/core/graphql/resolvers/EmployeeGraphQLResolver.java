package cn.asany.organization.core.graphql.resolvers;

import cn.asany.base.common.bean.Address;
import cn.asany.base.common.bean.Email;
import cn.asany.base.common.bean.Phone;
import cn.asany.organization.core.bean.Department;
import cn.asany.organization.core.bean.EmployeeStatus;
import cn.asany.organization.core.graphql.enums.EmployeeIdType;
import cn.asany.organization.employee.bean.Employee;
import cn.asany.organization.employee.bean.EmployeeAddress;
import cn.asany.organization.employee.bean.EmployeeEmail;
import cn.asany.organization.employee.bean.EmployeePhoneNumber;
import cn.asany.organization.relationship.bean.EmployeePosition;
import cn.asany.organization.relationship.bean.Position;
import graphql.kickstart.tools.GraphQLResolver;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * 员工 Resolver
 *
 * @author limaofeng
 */
@Component
public class EmployeeGraphQLResolver implements GraphQLResolver<Employee> {

  public String id(Employee employee, EmployeeIdType type) {
    return String.valueOf(employee.getId());
  }

  public EmployeeStatus status(Employee employee) {
    return null;
  }

  public Email email(Employee employee, String label) {
    Optional<EmployeeEmail> optional =
        employee.getEmails().stream()
            .filter(
                item -> {
                  if (label != null) {
                    return item.getLabel().equals(label);
                  } else {
                    return item.getPrimary();
                  }
                })
            .findFirst();
    return optional.isPresent() ? optional.get().getEmail() : null;
  }

  public Phone phone(Employee employee, String label) {
    Optional<EmployeePhoneNumber> optional =
        employee.getPhones().stream()
            .filter(
                item -> {
                  if (label != null) {
                    return item.getLabel().equals(label);
                  } else {
                    return item.getPrimary();
                  }
                })
            .findFirst();
    return optional.isPresent() ? optional.get().getPhone() : null;
  }

  public Address address(Employee employee, String label) {
    Optional<EmployeeAddress> optional =
        employee.getAddresses().stream()
            .filter(
                item -> {
                  if (label != null) {
                    return item.getLabel().equals(label);
                  } else {
                    return item.getPrimary();
                  }
                })
            .findFirst();
    return optional.isPresent() ? optional.get().getAddress() : null;
  }

  public Position primaryPosition(Employee employee) {
    Optional<EmployeePosition> primary =
        employee.getEmployeePositions().stream()
            .filter(ep -> ep.getPrimary() && "".equals(ep.getOrganization().getId()))
            .findAny();
    return primary.isPresent() ? primary.get().getPosition() : null;
  }

  public Department primaryDepartment(Employee employee) {
    Optional<EmployeePosition> primary =
        employee.getEmployeePositions().stream()
            .filter(ep -> ep.getPrimary() && "".equals(ep.getOrganization().getId()))
            .findAny();
    return primary.isPresent() ? primary.get().getDepartment() : null;
  }

  public List<Department> departments(Employee employee, String organization) {
    Stream<EmployeePosition> stream = employee.getEmployeePositions().stream();

    if (StringUtil.isNotBlank(organization)) {
      stream =
          stream.filter(
              employeePosition -> organization.equals(employeePosition.getOrganization().getId()));
    }
    return stream
        .map(EmployeePosition::getDepartment)
        .collect(
            collectingAndThen(
                toCollection(() -> new TreeSet<>(Comparator.comparing(Department::getId))),
                ArrayList::new));
  }

  public List<Position> positions(Employee employee, String organization, Long departmentId) {
    Stream<EmployeePosition> stream = employee.getEmployeePositions().stream();
    if (StringUtil.isNotBlank(organization)) {
      stream =
          stream.filter(
              employeePosition -> organization.equals(employeePosition.getOrganization().getId()));
    }
    if (departmentId != null) {
      stream =
          stream.filter(
              employeePosition -> departmentId.equals(employeePosition.getDepartment().getId()));
    }
    List<Position> collect =
        stream.map(employeePosition -> employeePosition.getPosition()).collect(Collectors.toList());
    return collect.stream().filter(item -> item.getId() != null).collect(Collectors.toList());
  }
}
