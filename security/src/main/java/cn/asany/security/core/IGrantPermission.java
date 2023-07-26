package cn.asany.security.core;

import cn.asany.base.common.SecurityType;
import cn.asany.security.core.domain.Permission;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
public interface IGrantPermission {
  /**
   * 权限
   *
   * @return 权限
   */
  Permission getPermission();

  /**
   * 权限类型
   *
   * @return 权限类型
   */
  SecurityType getSecurityType();

  /**
   * 授权给
   *
   * @return String
   */
  String getValue();

  /**
   * 授权资源
   *
   * @return String
   */
  String getResource();
}
