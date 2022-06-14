package cn.asany.cms.body.service;

import cn.asany.cms.article.domain.ArticleBody;
import cn.asany.cms.article.domain.enums.ArticleBodyType;

public interface ArticleBodyHandler {

  boolean supports(ArticleBodyType type);

  ArticleBody save(ArticleBody body);

  ArticleBody update(Long id, ArticleBody body);

  void delete(Long id);
}
