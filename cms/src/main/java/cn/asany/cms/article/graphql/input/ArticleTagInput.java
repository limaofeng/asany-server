package cn.asany.cms.article.graphql.input;

import cn.asany.storage.api.FileObject;
import lombok.Data;

/** 文章标签 */
@Data
public class ArticleTagInput {
  private String code;
  private String path;
  private String name;
  private FileObject cover;
  private String organization;
  private String description;
  private Integer sort;
  private ArticleMetadataInput metadata;
}
