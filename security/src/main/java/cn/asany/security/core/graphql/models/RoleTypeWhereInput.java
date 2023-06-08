package cn.asany.security.core.graphql.models;

import cn.asany.security.core.domain.enums.RoleType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

/**
 * @author: guoyong
 * @description: 角色分类查询过滤
 * @create: 2020/6/9 15:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleTypeWhereInput extends WhereInput<RoleTypeWhereInput, RoleType> {
}
