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
package cn.asany.pim.product.dao.listener;

import cn.asany.cms.article.domain.Article;
import cn.asany.pim.product.dao.ProductArticleDao;
import cn.asany.pim.product.domain.ProductArticle;
import java.util.List;
import net.asany.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

@Component("pim.articleDeleteListener")
public class ArticleDeleteListener extends AbstractChangedListener<Article> {

  private final ProductArticleDao productArticleDao;

  public ArticleDeleteListener(ProductArticleDao productArticleDao) {
    super(EventType.POST_DELETE);
    this.productArticleDao = productArticleDao;
  }

  public boolean requiresPostCommitHandling(EntityPersister persister) {
    return false;
  }

  @Override
  protected void onPostDelete(Article article, PostDeleteEvent event) {
    List<ProductArticle> productArticles =
        productArticleDao.findAll(PropertyFilter.newFilter().equal("article.id", article.getId()));
    if (productArticles.isEmpty()) {
      return;
    }
    productArticleDao.deleteAllInBatch(productArticles);
  }
}
