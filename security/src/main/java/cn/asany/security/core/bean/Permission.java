package cn.asany.security.core.bean;

import cn.asany.security.core.bean.enums.PermissionCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 权限配置信息
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUTH_PERMISSION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Permission extends BaseBusEntity {

  private static final long serialVersionUID = 2224908963065749499L;

  @Id
  @Column(name = "ID", length = 25)
  private String id;
  /** 权限名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 资源描述 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;
  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
  /** 类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "CATEGORY", length = 20, nullable = false)
  private PermissionCategory category;
  /** 资源类型 */
  @Column(name = "RESOURCE_TYPE", length = 25)
  private String resourceType;

  @Transient private List<GrantPermission> grants;

  /** 对应的权限分类 */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "PERMISSION_TYPE")
  private PermissionType permissionType;
}
