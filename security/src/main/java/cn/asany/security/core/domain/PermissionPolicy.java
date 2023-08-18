package cn.asany.security.core.domain;

import cn.asany.security.core.domain.enums.PermissionPolicyType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.Tenantable;

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
public class PermissionPolicy extends BaseBusEntity implements Tenantable {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 授权策略类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 10, nullable = false, updatable = false)
  private PermissionPolicyType type;
  /** 名称 */
  @Column(name = "NAME", length = 128, nullable = false)
  private String name;
  /** 备注 */
  @Column(name = "DESCRIPTION", length = 1024)
  private String description;
  /** 授权语句 */
  @OneToMany(
      mappedBy = "policy",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<PermissionStatement> statements;
  /** 租户ID */
  @Column(name = "TENANT_ID", length = 24, updatable = false)
  private String tenantId;
  /** 授权 */
  @OneToMany(mappedBy = "policy", fetch = FetchType.LAZY)
  private List<Permission> permissions;
}
