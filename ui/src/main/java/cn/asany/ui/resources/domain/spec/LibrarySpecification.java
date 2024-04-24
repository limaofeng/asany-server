// package cn.asany.ui.resources.domain.spec;
//
// import java.util.ArrayList;
// import java.util.List;
// import jakarta.persistence.criteria.CriteriaBuilder;
// import jakarta.persistence.criteria.CriteriaQuery;
// import jakarta.persistence.criteria.Predicate;
// import jakarta.persistence.criteria.Root;
// import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
// import org.hibernate.query.criteria.internal.ParameterRegistry;
// import org.hibernate.query.criteria.internal.compile.RenderingContext;
// import org.hibernate.query.criteria.internal.predicate.AbstractSimplePredicate;
// import org.jetbrains.annotations.NotNull;
// import net.asany.jfantasy.framework.util.common.StringUtil;
// import org.springframework.data.jpa.domain.Specification;
//
// public class LibrarySpecification implements Specification {
//  private final List<Long> libraries;
//
//  public LibrarySpecification(List<Long> libraries) {
//    this.libraries = libraries;
//  }
//
//  public LibrarySpecification(Long library) {
//    this.libraries = new ArrayList<>();
//    this.libraries.add(library);
//  }
//
//  @Override
//  public Predicate toPredicate(
//      @NotNull Root root, @NotNull CriteriaQuery query, @NotNull CriteriaBuilder builder) {
//    return new HQLPredicate(builder, this.libraries);
//  }
//
//  static class HQLPredicate extends AbstractSimplePredicate {
//    private final List<Long> libraries;
//
//    public HQLPredicate(CriteriaBuilder builder, List<Long> libraries) {
//      super((CriteriaBuilderImpl) builder);
//      this.libraries = libraries;
//    }
//
//    @Override
//    public String render(boolean isNegated, RenderingContext renderingContext) {
//      String ids =
//          StringUtil.join(
//              this.libraries.stream().map(Object::toString).toArray(String[]::new), ",");
//      return "generatedAlias0.id in (SELECT resourceId FROM LibraryItem li WHERE li.library in ("
//          + ids
//          + "))";
//    }
//
//    @Override
//    public void registerParameters(ParameterRegistry registry) {}
//  }
// }
