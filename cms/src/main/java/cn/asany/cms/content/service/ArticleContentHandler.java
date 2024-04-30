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
import java.util.Map;
import java.util.Optional;

public interface ArticleContentHandler<T extends ArticleContent> {

  boolean supports(ContentType type);

  T save(T body);

  T update(Long id, T body);

  void delete(Long id);

  T parse(Map<String, Object> content);

  Optional<T> findById(Long id);
}
