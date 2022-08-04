package cn.asany.organization.core.dao.specification;

import cn.asany.organization.employee.domain.Employee;
import cn.asany.organization.employee.domain.Star;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
public class ExcludeStarEmployeeSpecification implements Specification<Employee> {

  private String starType;
  private String galaxy;

  public ExcludeStarEmployeeSpecification(String starType, String galaxy) {
    this.starType = starType;
    this.galaxy = galaxy;
  }

  @Override
  public Predicate toPredicate(
      Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    Subquery<Star> subquery = query.subquery(Star.class);
    Root<Star> starRoot = subquery.from(Star.class);
    subquery.select(starRoot);

    // 排除已加星的数据
    List<Predicate> subQueryPredicates = new ArrayList<>();
    subQueryPredicates.add(builder.equal(root.get("id"), starRoot.get("stargazer")));
    subQueryPredicates.add(builder.equal(starRoot.get("starType").get("id"), this.starType));
    subQueryPredicates.add(builder.equal(starRoot.get("galaxy"), this.galaxy));
    subquery.where(subQueryPredicates.toArray(new Predicate[] {}));

    return builder.not(builder.exists(subquery));
  }
}
