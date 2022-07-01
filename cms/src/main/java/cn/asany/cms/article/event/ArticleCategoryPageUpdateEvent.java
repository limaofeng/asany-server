package cn.asany.cms.article.event;

import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.domain.PageComponent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

/**
 * 登录成功
 *
 * @author limaofeng
 */
public class ArticleCategoryPageUpdateEvent extends ApplicationEvent {

  public ArticleCategoryPageUpdateEvent(ArticleCategory category, PageComponent page) {
    super(ArticleCategoryPageUpdateSource.builder().category(category).page(page).build());
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ArticleCategoryPageUpdateSource {
    private ArticleCategory category;
    private PageComponent page;
  }
}
