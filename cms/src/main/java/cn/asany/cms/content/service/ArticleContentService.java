package cn.asany.cms.content.service;

import cn.asany.cms.article.domain.ArticleContent;
import cn.asany.cms.content.domain.enums.ContentType;
import cn.asany.cms.content.graphql.input.ArticleContentInput;
import java.util.List;
import net.asany.jfantasy.framework.error.ValidationException;
import net.asany.jfantasy.framework.util.common.ObjectUtil;

public class ArticleContentService {

  private final List<ArticleContentHandler<ArticleContent>> handlers;

  public ArticleContentService(List<ArticleContentHandler<ArticleContent>> handlers) {
    this.handlers = handlers;
  }

  public ArticleContent convert(ArticleContentInput content, ContentType contentType) {
    ArticleContentHandler<ArticleContent> handler = getContentHandler(contentType);
    return handler.parse(ObjectUtil.toMap(content));
  }

  public ArticleContentHandler<ArticleContent> getContentHandler(ContentType type) {
    return this.handlers.stream()
        .filter(item -> item.supports(type))
        .findFirst()
        .orElseThrow(() -> new ValidationException("100", type.name() + "没有对应的处理逻辑 "));
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
