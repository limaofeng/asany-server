package cn.asany.cms.permission.domain;

import java.util.List;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-12 09:18
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionScheme extends BaseBusEntity {
  /** 主键ID */
  private Long id;
  /** 名称 */
  private String name;
  /** 描述 */
  private String description;
  /** 权限 */
  private List<GrantPermission> grants;
}
