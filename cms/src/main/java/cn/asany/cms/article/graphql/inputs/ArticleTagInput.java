package cn.asany.cms.article.graphql.inputs;

import cn.asany.storage.api.FileObject;
import lombok.Data;

/** @Description @Author ChenWenJie @Data 2020/10/22 11:39 上午 */
@Data
public class ArticleTagInput {
  private String code;
  private String path;
  private String name;
  private FileObject cover;
  private String organization;
  private String description;
  private Integer sort;
}
