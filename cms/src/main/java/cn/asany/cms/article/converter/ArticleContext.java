package cn.asany.cms.article.converter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleContext {
  private String storeTemplate;
}
