package cn.asany.pim.product.service;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.graphql.input.ArticleWhereInput;
import cn.asany.pim.product.dao.ProductArticleDao;
import cn.asany.pim.product.domain.ProductArticle;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
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
    List<ProductArticle> productArticles =
        this.productArticleDao.findAll(filter, offset, limit, orderBy);
    return productArticles.stream().map(ProductArticle::getArticle).collect(Collectors.toList());
  }
}
