package cn.asany.security.core.graphql.input;

import cn.asany.security.core.domain.PermissionStatement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: guoyong
 * @description: 权限输入类
 * @create: 2020/6/9 17:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionStatementUpdateInput extends PermissionStatement {
  /** 权限分类输入 */
  private String permissionTypeInput;
}
