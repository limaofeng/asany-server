package cn.asany.cms.article.graphql.input;

import java.util.List;
import lombok.Data;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Data
public class PermissionInput {
  private String permission;
  private List<String> grants;
}
