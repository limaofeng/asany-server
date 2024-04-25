/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.organization.core.graphql.resolvers;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import cn.asany.base.common.domain.Address;
import cn.asany.base.common.domain.Email;
import cn.asany.base.common.domain.Phone;
import cn.asany.organization.core.domain.Department;
import cn.asany.organization.core.domain.EmployeeStatus;
import cn.asany.organization.core.graphql.enums.EmployeeIdType;
import cn.asany.organization.employee.domain.Employee;
import cn.asany.organization.employee.domain.EmployeeAddress;
import cn.asany.organization.employee.domain.EmployeeEmail;
import cn.asany.organization.employee.domain.EmployeePhoneNumber;
import cn.asany.organization.relationship.domain.EmployeePosition;
import cn.asany.organization.relationship.domain.Position;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

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
        employee.getPositions().stream()
            .filter(ep -> ep.getPrimary() && "".equals(ep.getOrganization().getId()))
            .findAny();
    return primary.isPresent() ? primary.get().getPosition() : null;
  }

  public Department primaryDepartment(Employee employee) {
    Optional<EmployeePosition> primary =
        employee.getPositions().stream()
            .filter(ep -> ep.getPrimary() && "".equals(ep.getOrganization().getId()))
            .findAny();
    return primary.isPresent() ? primary.get().getDepartment() : null;
  }

  public List<Department> departments(Employee employee, String organization) {
    Stream<EmployeePosition> stream = employee.getPositions().stream();

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
    Stream<EmployeePosition> stream = employee.getPositions().stream();
    if (StringUtil.isNotBlank(organization)) {
      stream =
          stream.filter(
              employeePosition ->
                  organization.equals(employeePosition.getOrganization().getCode()));
    }
    if (departmentId != null) {
      stream =
          stream.filter(
              employeePosition -> departmentId.equals(employeePosition.getDepartment().getId()));
    }
    List<Position> collect = stream.map(EmployeePosition::getPosition).collect(Collectors.toList());
    return collect.stream().filter(item -> item.getId() != null).collect(Collectors.toList());
  }
}
