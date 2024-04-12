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
