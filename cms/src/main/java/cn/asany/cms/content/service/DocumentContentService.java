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

import cn.asany.cms.content.dao.DocumentContentDao;
import cn.asany.cms.content.domain.DocumentContent;
import cn.asany.cms.content.domain.enums.ContentType;
import cn.asany.cms.content.domain.enums.DocumentType;
import cn.asany.cms.content.graphql.input.ArticleContentInput;
import cn.asany.storage.api.FileObject;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DocumentContentService implements ArticleContentHandler<DocumentContent> {
  private final DocumentContentDao documentContentDao;

  public DocumentContentService(DocumentContentDao documentContentDao) {
    this.documentContentDao = documentContentDao;
  }

  @Override
  public boolean supports(ContentType type) {
    return type == ContentType.DOCUMENT;
  }

  @Override
  public DocumentContent save(DocumentContent body) {
    return this.documentContentDao.save(body);
  }

  @Override
  public DocumentContent update(Long id, DocumentContent body) {
    body.setId(id);
    return this.documentContentDao.update(body);
  }

  @Override
  public void delete(Long id) {
    this.documentContentDao.deleteById(id);
  }

  @Override
  public DocumentContent parse(ArticleContentInput content) {
    Long id = content.getId();
    String url = content.getUrl();
    DocumentType type = DocumentType.valueOf(content.getType());
    Long size = content.getSize();
    String title = content.getTitle();
    String description = content.getDescription();
    FileObject document = content.getDocument();
    return DocumentContent.builder()
        .url(url)
        .type(type)
        .size(size)
        .title(title)
        .description(description)
        .id(id)
        .document(document)
        .build();
  }

  @Override
  public Optional<DocumentContent> findById(Long id) {
    return this.documentContentDao.findById(id);
  }
}
