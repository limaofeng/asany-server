package cn.asany.cms.content.service;

import cn.asany.cms.article.domain.ArticleContent;
import cn.asany.cms.content.domain.enums.ContentType;

public interface ArticleContentHandler<T extends ArticleContent> {

  boolean supports(ContentType type);

  T save(T body);

  T update(Long id, T body);

  void delete(Long id);
}