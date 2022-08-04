package cn.asany.security.core.graphql.input;

import lombok.Data;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Data
public class GrantPermissionByUserInput {
  private String permission;
  private String resource;
}
