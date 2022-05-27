package cn.asany.cms.permission.domain;

import java.util.List;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author: guoyong
 * @description: 权限分类表
 * @create: 2020/6/9 11:18
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionType extends BaseBusEntity {

  private static final long serialVersionUID = 2224908963065749111L;

  /** 未知分类存值 */
  public static final String UNKNOWN = "UNKNOWN";

  /** 权限分类编码 */
  private String id;
  /** 权限分类名称 */
  private String name;

  /** 描述信息 */
  private String description;

  /** 排序 */
  private int sort;

  /** 对应的权限定义 */
  private List<Permission> permissions;
}
