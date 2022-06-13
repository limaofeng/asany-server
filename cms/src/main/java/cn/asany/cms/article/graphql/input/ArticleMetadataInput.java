package cn.asany.cms.article.graphql.input;

import cn.asany.storage.api.FileObject;
import lombok.Data;

@Data
public class ArticleMetadataInput {
  private FileObject image;
  private String url;
  private String title;
  private String description;
}
