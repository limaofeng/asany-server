package cn.asany.security.core.graphql.input;

import cn.asany.base.common.SecurityType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.predicate.AbstractSimplePredicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
public class GrantPermissionSpecification implements Specification {
  private List<String> resources;
  private String permission;
  private SecurityType securityType;

  public GrantPermissionSpecification(
      String permission, SecurityType securityType, List<String> resources) {
    this.permission = permission;
    this.securityType = securityType;
    this.resources = resources;
  }

  @Override
  public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
    return new HQLPredicate(builder, this.permission, this.securityType, this.resources);
  }

  static class HQLPredicate extends AbstractSimplePredicate {
    private List<String> resources;
    private String permission;
    private SecurityType securityType;

    public HQLPredicate(
        CriteriaBuilder builder,
        String permission,
        SecurityType securityType,
        List<String> resources) {
      super((CriteriaBuilderImpl) builder);
      this.permission = permission;
      this.securityType = securityType;
      this.resources = resources;
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
      String hql =
          "select value FROM GrantPermission gp WHERE gp.permission = '"
              + permission
              + "' and gp.securityType = '"
              + securityType
              + "' and gp.resource in ("
              + resources.stream().collect(Collectors.joining(","))
              + ")";
      return "generatedAlias0.id in (" + hql + ")";
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {}
  }
}
