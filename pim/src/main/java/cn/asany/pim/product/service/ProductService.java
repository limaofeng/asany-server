package cn.asany.pim.product.service;

import cn.asany.cms.article.domain.Article;
import cn.asany.pim.product.dao.ProductArticleDao;
import cn.asany.pim.product.dao.ProductDao;
import cn.asany.pim.product.dao.ProductLinkageTypeDao;
import cn.asany.pim.product.domain.Product;
import cn.asany.pim.product.domain.ProductArticle;
import cn.asany.pim.product.domain.ProductImage;
import cn.asany.pim.product.domain.ProductLinkageType;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductDao productDao;

  private final ProductArticleDao productArticleDao;

  private final ProductLinkageTypeDao productLinkageTypeDao;

  public ProductService(
      ProductDao productDao,
      ProductArticleDao productArticleDao,
      ProductLinkageTypeDao productLinkageTypeDao) {
    this.productDao = productDao;
    this.productArticleDao = productArticleDao;
    this.productLinkageTypeDao = productLinkageTypeDao;
  }

  public Page<Product> findPage(Pageable pageable, PropertyFilter filter) {
    return this.productDao.findPage(pageable, filter);
  }

  public Product save(Product product) {
    for (ProductImage image : product.getImages()) {
      image.setProduct(product);
      image.setIndex(product.getImages().indexOf(image));
    }
    return this.productDao.save(product);
  }

  public Product update(Long id, Product product, Boolean merge) {
    product.setId(id);

    for (ProductImage image : product.getImages()) {
      image.setProduct(product);
      image.setIndex(product.getImages().indexOf(image));
    }

    return this.productDao.update(product, merge);
  }

  public List<Product> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return this.productDao.findAll(filter, offset, limit, sort);
  }

  public Optional<Product> findBySn(String id) {
    return this.productDao.findOneBy("sn", id);
  }

  public Optional<Product> findById(Long id) {
    return this.productDao.findById(id);
  }

  public Optional<Product> delete(Long id) {
    Optional<Product> product = this.productDao.findById(id);
    product.ifPresent(this.productDao::delete);
    return product;
  }

  public Optional<Product> addArticlesToProduct(
      Long productId, String linkType, List<Long> articleIds) {
    return this.productDao
        .findById(productId)
        .map(
            product -> {
              ProductLinkageType linkageType =
                  this.productLinkageTypeDao
                      .findOneBy("code", linkType)
                      .orElseThrow(() -> new IllegalArgumentException("关联类型不存在"));
              for (Long articleId : articleIds) {
                if (this.productArticleDao.exists(
                    PropertyFilter.newFilter()
                        .equal("product.id", productId)
                        .equal("article.id", articleId)
                        .equal("linkageType.id", linkageType.getId()))) {
                  continue;
                }
                this.productArticleDao.save(
                    ProductArticle.builder()
                        .article(Article.builder().id(articleId).build())
                        .product(product)
                        .linkageType(linkageType)
                        .build());
              }
              return product;
            });
  }

  public Optional<Product> removeArticlesFromProduct(
      Long productId, String linkType, List<Long> articleIds) {
    return this.productDao
        .findById(productId)
        .map(
            product -> {
              ProductLinkageType linkageType =
                  this.productLinkageTypeDao
                      .findOneBy("code", linkType)
                      .orElseThrow(() -> new IllegalArgumentException("关联类型不存在"));
              for (Long articleId : articleIds) {
                this.productArticleDao
                    .findOne(
                        PropertyFilter.newFilter()
                            .equal("product.id", productId)
                            .equal("article.id", articleId)
                            .equal("linkageType.id", linkageType.getId()))
                    .ifPresent(this.productArticleDao::delete);
              }
              return product;
            });
  }
}
