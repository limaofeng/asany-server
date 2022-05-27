package cn.asany.cms.article.event;

import cn.asany.cms.article.domain.Article;
import org.springframework.context.ApplicationEvent;

/**
 * @author limaofeng
 * @version V1.0
 */
public class ArticleCreatedEvent extends ApplicationEvent {

  ArticleCreatedEvent(Long id) {
    super(id);
  }

  public String getArticleId() {
    return this.getSource().toString();
  }

  public static ArticleCreatedEvent newInstance(Article article) {
    return new ArticleCreatedEvent(article.getId());
  }
}
