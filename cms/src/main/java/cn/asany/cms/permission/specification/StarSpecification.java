package cn.asany.cms.permission.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
public class StarSpecification implements Specification {
  private Long stargazer;
  private String starType;

  public StarSpecification(Long stargazer, String starType) {
    this.stargazer = stargazer;
    this.starType = starType;
  }

  @Override
  public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
    return new HQLPredicate(builder, this.stargazer, this.starType);
  }

  static class HQLPredicate extends AbstractSimplePredicate {
    private Long stargazer;
    private String starType;

    public HQLPredicate(CriteriaBuilder builder, Long stargazer, String starType) {
      super((CriteriaBuilderImpl) builder);
      this.stargazer = stargazer;
      this.starType = starType;
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
      String hql =
          "SELECT galaxy FROM Star s WHERE s.stargazer = "
              + stargazer
              + " and s.starType = '"
              + starType
              + "'";
      return "generatedAlias0.id in (" + hql + ")";
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {}
  }
}
