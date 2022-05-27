package cn.asany.security.core.domain.enums;

public enum PermissionGrantType {
  /** 通用授权 - 理论上不对应任何特定资源 */
  GENERAL,
  /** 权限包含特定资源 */
  RESOURCE
}
