package cn.asany.cms.article.graphql.type;

import cn.asany.cms.article.bean.Content;
import cn.asany.cms.article.bean.enums.ContentType;
import lombok.Data;

@Data
public class HtmlContent implements IContent {

  private Long id;

  private ContentType type;

  private String html;

  public HtmlContent(Content content) {
    this.html = content.getText();
  }
}
