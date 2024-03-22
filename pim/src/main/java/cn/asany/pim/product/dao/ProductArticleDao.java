package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.ProductArticle;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.ProductArticleDao")
public interface ProductArticleDao extends JpaRepository<ProductArticle, Long> {}
