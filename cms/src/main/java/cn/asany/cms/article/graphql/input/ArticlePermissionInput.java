package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.graphql.enums.ArticlePermissionKey;
import java.util.List;
import lombok.Data;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Data
public class ArticlePermissionInput {
  private ArticlePermissionKey permission;
  private List<String> grants;
}
