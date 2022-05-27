package cn.asany.security.core.graphql.models;

import cn.asany.security.core.domain.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-05-05 09:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleInput extends Role {

  private String scopes;

  private String roleTypeInput;
}
