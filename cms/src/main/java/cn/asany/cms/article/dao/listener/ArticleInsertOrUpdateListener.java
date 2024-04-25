/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
