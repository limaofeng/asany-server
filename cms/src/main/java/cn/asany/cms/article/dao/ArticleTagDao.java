package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.ArticleTag;
import org.apache.ibatis.annotations.Param;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleTagDao extends JpaRepository<ArticleTag, Long> {

  @Modifying
  @Query(nativeQuery = true, value = "delete from cms_article_channel where channel_id=?1")
  void deleteArticleChannel(@Param("channelId") Long channelId);

  @Modifying
  @Query(nativeQuery = true, value = "delete from cms_article_tags where tag_id=?1")
  void deleteArticleTags(@Param("tagId") Long tagId);
}
