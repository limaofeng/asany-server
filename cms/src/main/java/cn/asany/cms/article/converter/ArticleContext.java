package cn.asany.cms.article.converter;

import cn.asany.cms.article.domain.ArticleStoreTemplate;
import cn.asany.cms.content.domain.enums.ContentType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleContext {
  private ArticleStoreTemplate storeTemplate;
  private ContentType contentType;
}
