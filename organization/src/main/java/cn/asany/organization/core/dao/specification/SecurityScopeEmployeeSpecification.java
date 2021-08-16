package cn.asany.organization.core.dao.specification;

import cn.asany.base.common.SecurityScope;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.data.jpa.domain.Specification;

/**
 * 权限筛选
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-08-20 14:10
 */
@Slf4j
public class SecurityScopeEmployeeSpecification<T> implements Specification<T> {

  private String employeeKey;
  private List<SecurityScope> securityScopes;

  public SecurityScopeEmployeeSpecification(List<SecurityScope> securityScopes) {
    this.securityScopes = securityScopes;
  }

  public SecurityScopeEmployeeSpecification(
      String employeeKey, List<SecurityScope> securityScopes) {
    this.securityScopes = securityScopes;
    this.employeeKey = employeeKey;
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    List<Predicate> predicates = new ArrayList<>();

    From path = root;
    if (StringUtil.isNotBlank(employeeKey)) {
      for (String name : StringUtil.tokenizeToStringArray(employeeKey, ".")) {
        path = path.join(name, JoinType.LEFT);
      }
    }

    for (SecurityScope scope : this.securityScopes) {
      switch (scope.getType()) {
        case organization:
          Join organizationJoin = path.join("employeePositions", JoinType.LEFT);
          predicates.add(
              builder.equal(organizationJoin.get("organization").get("id"), scope.getValue()));
          break;
        case department:
          Join departmentJoin = path.join("employeePositions", JoinType.LEFT);
          predicates.add(
              builder.equal(departmentJoin.get("department").get("id"), scope.getValue()));
          break;
        case employeeGroup:
          Join groupJoin = path.join("groups", JoinType.LEFT);
          predicates.add(builder.equal(groupJoin.get("id"), Long.valueOf(scope.getValue())));
          break;
        case employee:
          predicates.add(builder.equal(path.get("id"), Long.valueOf(scope.getValue())));
          break;
        default:
          log.debug(scope.getType() + ", 暂不支持");
      }
    }

    return builder.or(predicates.stream().toArray(Predicate[]::new));
  }
}
