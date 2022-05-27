package cn.asany.cms.permission.domain;

import cn.asany.cms.permission.domain.enums.PermissionCategory;
import java.util.List;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 权限配置信息
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseBusEntity {

  private static final long serialVersionUID = 2224908963065749499L;

  private String id;
  /** 权限名称 */
  private String name;
  /** 资源描述 */
  private String description;
  /** 是否启用 */
  private Boolean enabled;
  /** 类型 */
  private PermissionCategory category;
  /** 资源类型 */
  private String resourceType;

  private List<GrantPermission> grants;

  /** 对应的权限分类 */
  private PermissionType permissionType;
}
