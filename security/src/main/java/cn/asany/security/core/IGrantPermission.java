package cn.asany.security.core;

import cn.asany.base.common.SecurityType;
import cn.asany.security.core.bean.Permission;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-27 10:09
 */
public interface IGrantPermission {
  /**
   * 权限
   *
   * @return
   */
  Permission getPermission();

  /**
   * 权限类型
   *
   * @return
   */
  SecurityType getSecurityType();

  /**
   * 授权给
   *
   * @return
   */
  String getValue();

  /**
   * 授权资源
   *
   * @return
   */
  String getResource();
}
