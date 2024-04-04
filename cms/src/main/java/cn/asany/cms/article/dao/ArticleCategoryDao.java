package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.ArticleCategory;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCategoryDao extends JpaRepository<ArticleCategory, Long> {

  @Modifying
  @Query(
      nativeQuery = true,
      value = "delete from cms_article_category where channel_id = :channelId")
  void deleteArticleChannel(@Param("channelId") Long channelId);

  @Modifying
  @Query(nativeQuery = true, value = "delete from cms_article_tags where tag_id = :tagId")
  void deleteArticleTags(@Param("tagId") Long tagId);

  @Modifying
  @Query(
      nativeQuery = true,
      value =
          "delete from cms_article_category where path like :path% and id != :id ORDER BY `level` DESC")
  void deleteAllByPath(@Param("path") String s, @Param("id") Long id);

  @Query(
      nativeQuery = true,
      value = "SELECT application_id FROM website WHERE article_channel_id = :channel_id")
  Long getAppIdOfWebsite(@Param("channel_id") Long categoryId);
}
