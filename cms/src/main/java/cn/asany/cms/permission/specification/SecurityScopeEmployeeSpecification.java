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
