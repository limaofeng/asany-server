package cn.asany.security.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 权限策略
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_PERMISSION_POLICY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PermissionPolicy extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 128, nullable = false)
  private String name;
  /** 备注 */
  @Column(name = "DESCRIPTION", length = 1024)
  private String description;

  @OneToMany(mappedBy = "policy", fetch = FetchType.LAZY)
  private List<PermissionStatement> permissionStatements;

  @OneToMany(mappedBy = "permissionPolicy", fetch = FetchType.LAZY)
  private List<GrantPermission> grantPermissions;
}
