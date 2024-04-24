package cn.asany.pim.product.service;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.graphql.input.ArticleWhereInput;
import cn.asany.pim.product.dao.ProductArticleDao;
import cn.asany.pim.product.domain.ProductArticle;
import java.util.List;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.dao.MatchType;
import net.asany.jfantasy.framework.dao.jpa.JpaDefaultPropertyFilter;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.dao.jpa.PropertyPredicate;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductArticleService {

  private final ProductArticleDao productArticleDao;

  public ProductArticleService(ProductArticleDao productArticleDao) {
    this.productArticleDao = productArticleDao;
  }

  public List<Article> findArticles(
      Long productId,
      String linkageType,
      ArticleWhereInput where,
      int offset,
      int limit,
      Sort orderBy) {
    PropertyFilter filter =
        PropertyFilter.newFilter()
            .equal("product.id", productId)
            .equal("linkageType.code", linkageType);

    String keyword = getKeyword(where);
    if (StringUtil.isNotBlank(keyword)) {
      filter.or(
          PropertyFilter.newFilter().contains("article.summary", keyword),
          PropertyFilter.newFilter().contains("article.title", keyword));
    }

    List<ProductArticle> productArticles =
        this.productArticleDao.findAll(filter, offset, limit, orderBy);
    return productArticles.stream().map(ProductArticle::getArticle).collect(Collectors.toList());
  }

  private String getKeyword(ArticleWhereInput where) {
    PropertyFilter articleFilter = where.toFilter();
    List<PropertyPredicate> predicates = articleFilter.build();

    PropertyPredicate predicate = ObjectUtil.find(predicates, "matchType", MatchType.OR);
    if (predicate != null) {
      List<JpaDefaultPropertyFilter> filters = predicate.getPropertyValue();
      for (JpaDefaultPropertyFilter f : filters) {
        List<PropertyPredicate> predicates1 = f.build();
        return predicates1.get(0).getPropertyValue();
      }
    }
    return null;
  }
}
