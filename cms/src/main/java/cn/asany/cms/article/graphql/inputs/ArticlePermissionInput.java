package cn.asany.cms.article.graphql.inputs;

import cn.asany.cms.article.graphql.enums.ArticlePermissionKey;
import java.util.List;
import lombok.Data;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-08-19 15:17
 */
@Data
public class ArticlePermissionInput {
  private ArticlePermissionKey permission;
  private List<String> grants;
}
