package cn.asany.cms.article.graphql.type;

import cn.asany.cms.article.bean.enums.ContentType;

public interface IContent {
  Long getId();

  void setId(Long id);

  ContentType getType();

  void setType(ContentType type);
}
