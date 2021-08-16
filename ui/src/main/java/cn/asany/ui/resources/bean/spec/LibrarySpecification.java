package cn.asany.ui.resources.bean.spec;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.predicate.AbstractSimplePredicate;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.data.jpa.domain.Specification;

public class LibrarySpecification implements Specification {
  private List<Long> libraries;

  public LibrarySpecification(List<Long> libraries) {
    this.libraries = libraries;
  }

  public LibrarySpecification(Long library) {
    this.libraries = new ArrayList<>();
    this.libraries.add(library);
  }

  @Override
  public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
    return new HQLPredicate(builder, this.libraries);
  }

  static class HQLPredicate extends AbstractSimplePredicate {
    private List<Long> libraries;

    public HQLPredicate(CriteriaBuilder builder, List<Long> libraries) {
      super((CriteriaBuilderImpl) builder);
      this.libraries = libraries;
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
      String ids =
          StringUtil.join(
              this.libraries.stream().map(item -> item.toString()).toArray(String[]::new), ",");
      return "generatedAlias0.id in (SELECT resourceId FROM LibraryItem li WHERE li.library in ("
          + ids
          + "))";
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {}
  }
}
