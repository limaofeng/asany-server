package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.bean.MetaData;
import cn.asany.cms.article.bean.enums.PromptType;
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
public class ArticleChannelInput {
  private String name;
  private String description;
  private String slug;
  private Integer sort;
  private List<PermissionInput> permissions;
  private Long parent;
  private String organization;
  private FileObject cover;
  private PromptType promptType;
  private Boolean isCommentApprove;
  private String approveId;
  private MetaData meta;
}
