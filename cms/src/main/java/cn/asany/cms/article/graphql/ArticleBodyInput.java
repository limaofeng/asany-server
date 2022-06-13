package cn.asany.cms.article.graphql;

import cn.asany.cms.article.domain.enums.ArticleBodyType;
import lombok.Data;

@Data
public class ArticleBodyInput {
  private ArticleBodyType type;
  private String source;
}
