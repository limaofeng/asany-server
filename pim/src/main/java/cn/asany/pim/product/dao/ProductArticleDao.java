package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.ProductArticle;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.ProductArticleDao")
public interface ProductArticleDao extends AnyJpaRepository<ProductArticle, Long> {}
