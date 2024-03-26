package cn.asany.cms.content.service;

import cn.asany.cms.article.domain.ArticleContent;
import cn.asany.cms.content.domain.TextContent;
import cn.asany.cms.content.domain.enums.ContentType;
import java.util.List;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.jackson.JSON;

public class ArticleContentService {

  private final List<ArticleContentHandler<ArticleContent>> handlers;

  public ArticleContentService(List<ArticleContentHandler<ArticleContent>> handlers) {
    this.handlers = handlers;
  }

  public ArticleContent convert(String body, ContentType contentType) {
    if (contentType == ContentType.TEXT) {
      return JSON.deserialize(body, TextContent.class);
    }
    throw new IllegalStateException("Unexpected value: " + body);
  }

  public ArticleContentHandler<ArticleContent> getBodyHandler(ContentType type) {
    return this.handlers.stream()
        .filter(item -> item.supports(type))
        .findFirst()
        .orElseThrow(() -> new ValidationException("100", type.name() + "没有对应的处理逻辑 "));
  }

  public ArticleContent save(ArticleContent content) {
    ArticleContentHandler<ArticleContent> handler = getBodyHandler(content.getContentType());
    return handler.save(content);
  }

  public ArticleContent update(Long id, ArticleContent content) {
    ArticleContentHandler<ArticleContent> handler = getBodyHandler(content.getContentType());
    return handler.update(id, content);
  }

  public void deleteById(Long id, ContentType contentType) {
    ArticleContentHandler<ArticleContent> handler = getBodyHandler(contentType);
    handler.delete(id);
  }
}
