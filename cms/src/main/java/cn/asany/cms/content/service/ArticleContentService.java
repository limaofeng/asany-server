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
package cn.asany.cms.content.service;

import cn.asany.cms.article.domain.ArticleContent;
import cn.asany.cms.content.domain.enums.ContentType;
import cn.asany.cms.content.graphql.input.ArticleContentInput;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.error.ValidationException;
import net.asany.jfantasy.framework.util.common.ObjectUtil;

public class ArticleContentService {

  private final List<ArticleContentHandler<ArticleContent>> handlers;

  public ArticleContentService(List<ArticleContentHandler<ArticleContent>> handlers) {
    this.handlers = handlers;
  }

  public ArticleContent convert(ArticleContentInput content, ContentType contentType) {
    if (content == null) {
      return null;
    }
    ArticleContentHandler<ArticleContent> handler = getContentHandler(contentType);
    return handler.parse(ObjectUtil.toMap(content));
  }

  public ArticleContentHandler<ArticleContent> getContentHandler(ContentType type) {
    return this.handlers.stream()
        .filter(item -> item.supports(type))
        .findFirst()
        .orElseThrow(() -> new ValidationException("100", type.name() + "没有对应的处理逻辑 "));
  }

  public Optional<ArticleContent> findById(ContentType contentType, Long contentId) {
    ArticleContentHandler<ArticleContent> handler = getContentHandler(contentType);
    return handler.findById(contentId);
  }

  public ArticleContent save(ArticleContent content) {
    ArticleContentHandler<ArticleContent> handler = getContentHandler(content.getContentType());
    return handler.save(content);
  }

  public ArticleContent update(Long id, ArticleContent content) {
    ArticleContentHandler<ArticleContent> handler = getContentHandler(content.getContentType());
    return handler.update(id, content);
  }

  public void deleteById(Long id, ContentType contentType) {
    ArticleContentHandler<ArticleContent> handler = getContentHandler(contentType);
    handler.delete(id);
  }
}
