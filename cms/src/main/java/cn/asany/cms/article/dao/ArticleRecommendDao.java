package cn.asany.cms.article.dao;

import cn.asany.cms.article.bean.ArticleRecommend;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/** @Description 接口 @Author ChenWenJie @Data 2020/10/23 6:06 下午 */
@Repository
public interface ArticleRecommendDao extends JpaRepository<ArticleRecommend, Long> {}
