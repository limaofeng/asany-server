package cn.asany.security.core.graphql.input;

import cn.asany.security.core.domain.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.inputs.WhereInput;

/**
 * @author: guoyong
 * @description: 角色查询过滤
 * @create: 2020/6/9 15:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleWhereInput extends WhereInput<RoleWhereInput, Role> {}
