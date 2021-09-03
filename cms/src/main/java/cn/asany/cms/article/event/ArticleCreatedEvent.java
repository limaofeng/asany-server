package cn.asany.cms.article.event;

import cn.asany.cms.article.bean.Article;
import org.springframework.context.ApplicationEvent;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019/11/27 11:46 上午
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
