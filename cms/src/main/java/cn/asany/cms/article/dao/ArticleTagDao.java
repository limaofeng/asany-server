package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.ArticleTag;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleTagDao extends AnyJpaRepository<ArticleTag, Long> {

  @Modifying
  @Query(nativeQuery = true, value = "delete from cms_article_channel where channel_id=?1")
  void deleteArticleChannel(@Param("channelId") Long channelId);

  @Modifying
  @Query(nativeQuery = true, value = "delete from cms_article_tags where tag_id=?1")
  void deleteArticleTags(@Param("tagId") Long tagId);
}
