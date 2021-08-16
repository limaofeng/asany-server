package cn.asany.security.core.graphql.models;

import cn.asany.security.core.bean.Permission;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: guoyong
 * @description: 权限输入类
 * @create: 2020/6/9 17:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionUpdateInput extends Permission {
  /** 权限分类输入 */
  private String permissionTypeInput;
}
