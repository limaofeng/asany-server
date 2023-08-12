package cn.asany.security.core.convert;

import cn.asany.security.core.domain.Role;
import cn.asany.security.core.graphql.input.RoleCreateInput;
import cn.asany.security.core.graphql.input.RoleUpdateInput;

public interface RoleConverter {
  Role toRole(RoleCreateInput input);

  Role toRole(RoleUpdateInput input);
}
