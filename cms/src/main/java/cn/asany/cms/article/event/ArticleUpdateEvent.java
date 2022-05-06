package cn.asany.cms.article.event;

import cn.asany.cms.article.bean.Article;
import org.springframework.context.ApplicationEvent;

/**
 * @author limaofeng
 * @version V1.0
 */
public class ArticleUpdateEvent extends ApplicationEvent {

  public ArticleUpdateEvent(Article article) {
    super(article);
  }

  public String getArticleId() {
    return this.getSource().toString();
  }

  public static ArticleUpdateEvent newInstance(Article article) {
    return new ArticleUpdateEvent(article);
  }
}
