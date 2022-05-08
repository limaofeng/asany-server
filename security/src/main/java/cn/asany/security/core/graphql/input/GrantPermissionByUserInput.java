package cn.asany.security.core.graphql.input;

import lombok.Data;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-07-25 18:27
 */
@Data
public class GrantPermissionByUserInput {
  private String permission;
  private String resource;
}
