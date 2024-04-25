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
package cn.asany.cms.article.service;

import cn.asany.cms.article.dao.ArticleStoreTemplateDao;
import cn.asany.cms.article.domain.ArticleStoreTemplate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArticleStoreTemplateService {

  private final ArticleStoreTemplateDao articleStoreTemplateDao;

  public ArticleStoreTemplateService(ArticleStoreTemplateDao articleStoreTemplateDao) {
    this.articleStoreTemplateDao = articleStoreTemplateDao;
  }

  public List<ArticleStoreTemplate> storeTemplates() {
    return this.articleStoreTemplateDao.findAll(Sort.by("index").ascending());
  }

  public Optional<ArticleStoreTemplate> findById(Long id) {
    return this.articleStoreTemplateDao.findById(id);
  }
}
