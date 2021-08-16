package cn.asany.security.core.bean.enums;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-12 09:24
 */
public enum PermissionCategory {
  /** 通用权限，不用与具体资源挂钩 */
  universal,
  /** 资源权限，必须明确资源 */
  resource
}
