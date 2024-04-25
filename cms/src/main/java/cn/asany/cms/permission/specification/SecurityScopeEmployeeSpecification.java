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
package cn.asany.cms.permission.specification;

import cn.asany.cms.permission.service.SecurityScope;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

/**
 * 安全范围员工查询条件
 *
 * @author limaofeng
 * @version V1.0
 */
@Slf4j
public class SecurityScopeEmployeeSpecification<T> implements Specification<T> {

  private String employeeKey;
  private final List<SecurityScope> securityScopes;

  public SecurityScopeEmployeeSpecification(List<SecurityScope> securityScopes) {
    this.securityScopes = securityScopes;
  }

  public SecurityScopeEmployeeSpecification(
      String employeeKey, List<SecurityScope> securityScopes) {
    this.securityScopes = securityScopes;
    this.employeeKey = employeeKey;
  }

  @Override
  public Predicate toPredicate(
      @NotNull Root<T> root, @NotNull CriteriaQuery<?> query, @NotNull CriteriaBuilder builder) {
    List<Predicate> predicates = new ArrayList<>();

    From<T, T> path = root;
    if (StringUtil.isNotBlank(employeeKey)) {
      for (String name : StringUtil.tokenizeToStringArray(employeeKey, ".")) {
        path = path.join(name, JoinType.LEFT);
      }
    }

    for (SecurityScope scope : this.securityScopes) {
      switch (scope.getType()) {
        case organization:
          Join<Object, Object> organizationJoin = path.join("employeePositions", JoinType.LEFT);
          predicates.add(
              builder.equal(organizationJoin.get("organization").get("id"), scope.getValue()));
          break;
        case department:
          Join<Object, Object> departmentJoin = path.join("employeePositions", JoinType.LEFT);
          predicates.add(
              builder.equal(departmentJoin.get("department").get("id"), scope.getValue()));
          break;
        case employeeGroup:
          Join<Object, Object> groupJoin = path.join("groups", JoinType.LEFT);
          predicates.add(builder.equal(groupJoin.get("id"), Long.valueOf(scope.getValue())));
          break;
        case employee:
          predicates.add(builder.equal(path.get("id"), Long.valueOf(scope.getValue())));
          break;
        default:
          log.debug(scope.getType() + ", 暂不支持");
      }
    }

    return builder.or(predicates.toArray(new Predicate[0]));
  }
}
