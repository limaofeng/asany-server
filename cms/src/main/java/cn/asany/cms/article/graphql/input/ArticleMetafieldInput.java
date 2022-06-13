package cn.asany.cms.article.graphql.input;

import lombok.Data;

@Data
public class ArticleMetafieldInput {
  private String key;
  private String type;
  private String namespace;
  private String value;
  private String description;
}
