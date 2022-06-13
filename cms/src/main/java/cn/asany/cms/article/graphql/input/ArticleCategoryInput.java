package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.domain.enums.PromptType;
import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.Data;

/**
 * 文章栏目新增对象
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-06-27 13:56
 */
@Data
public class ArticleCategoryInput {
  private String name;
  private String description;
  private String slug;
  private Integer index;
  private List<PermissionInput> permissions;
  private Long parent;
  private FileObject image;
  private PromptType promptType;
  private Boolean isCommentApprove;
  private String approveId;
  private Long storeTemplate;
  private ArticleMetadataInput metadata;
}
