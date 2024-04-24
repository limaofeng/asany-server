package cn.asany.cms.article.dao.listener;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.event.ArticleCreatedEvent;
import net.asany.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.springframework.stereotype.Component;

/**
 * 文章新增修改拦截
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
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
