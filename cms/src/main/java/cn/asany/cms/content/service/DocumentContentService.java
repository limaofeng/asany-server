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
import cn.asany.storage.api.FileObject;
import java.util.Map;
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
  public DocumentContent parse(Map<String, Object> content) {
    Long id = (Long) content.get("id");
    String url = (String) content.get("url");
    DocumentType type = DocumentType.valueOf((String) content.get("type"));
    Long size = (Long) content.get("size");
    String title = (String) content.get("title");
    String description = (String) content.get("description");
    FileObject document = (FileObject) content.get("video");
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
}
