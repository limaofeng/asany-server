package cn.asany.cms.article.dao.listener;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.event.ArticleCreatedEvent;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019/11/27 11:41 上午
 */
@Component
public class ArticleInsertOrUpdateListener extends AbstractChangedListener<Article> {

  public ArticleInsertOrUpdateListener() {
    super(EventType.POST_COMMIT_INSERT);
  }

  @Override
  protected void onPostInsert(Article entity, PostInsertEvent event) {
    this.applicationContext.publishEvent(ArticleCreatedEvent.newInstance(entity));
  }

  @Override
  public void onPostUpdate(Article entity, PostUpdateEvent event) {
    //   不触发😯
    //   this.applicationContext.publishEvent(ArticleUpdateEvent.newInstance(entity));
  }
}
