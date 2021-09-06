package cn.asany.cms.permission.bean;

import cn.asany.cms.permission.bean.enums.SecurityType;
import javax.persistence.Id;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-11 20:10
 */
@Getter
@Setter
@EqualsAndHashCode(
    of = {"securityType", "value", "resource", "permission"},
    callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrantPermission extends BaseBusEntity {

  @Id private Long id;
  /** 授权类型 */
  private SecurityType securityType;
  /** 授权 */
  private String value;
  /** 资源 */
  private String resource;
  /** 权限 */
  private Permission permission;
  /** 权限方案 */
  private PermissionScheme scheme;
}
