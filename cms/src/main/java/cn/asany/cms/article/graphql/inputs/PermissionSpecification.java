package cn.asany.cms.article.graphql.inputs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.predicate.AbstractSimplePredicate;
import org.springframework.data.jpa.domain.Specification;

public class PermissionSpecification implements Specification {

  private Map<String, String> typeValue;
  private String permission;

  public PermissionSpecification(String permission, Map<String, String> typeValue) {
    this.permission = permission;
    this.typeValue = typeValue;
  }

  @Override
  public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
    return new HQLPredicate(builder, this.permission, this.typeValue);
  }

  static class HQLPredicate extends AbstractSimplePredicate {
    private Map<String, String> typeValue;
    private String permission;

    public HQLPredicate(CriteriaBuilder builder, String permission, Map<String, String> typeValue) {
      super((CriteriaBuilderImpl) builder);
      this.permission = permission;
      this.typeValue = typeValue;
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
      List<String> dataList = new ArrayList<>();
      for (Map.Entry<String, String> entry : typeValue.entrySet()) {
        String aa =
            "(gp.securityType = '"
                + entry.getKey()
                + "' and gp.value = '"
                + entry.getValue()
                + "')";
        dataList.add(aa);
      }
      String hql =
          "select resource FROM GrantPermission gp WHERE gp.permission = '"
              + permission
              + "' and ("
              + dataList.stream().collect(Collectors.joining(" or "))
              + ")";
      return "generatedAlias0.id in (" + hql + ")";
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {}
  }
}
