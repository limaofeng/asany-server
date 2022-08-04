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
 * @date 2022/7/28 9:12 9:12
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
  private String storeTemplate;
  private List<ArticleMetafieldInput> metafields;
  private ArticleMetadataInput metadata;
  private PageComponentInput page;
}
