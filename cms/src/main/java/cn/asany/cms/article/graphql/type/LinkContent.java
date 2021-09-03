package cn.asany.cms.article.graphql.type;

import cn.asany.cms.article.bean.Content;
import cn.asany.cms.article.bean.enums.ContentType;
import lombok.Data;

@Data
public class LinkContent implements IContent {
  private Long id;
  private ContentType type;
  private String url;

  public LinkContent(Content content) {
    this.url = content.getText();
  }
}
