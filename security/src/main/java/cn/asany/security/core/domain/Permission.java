package cn.asany.security.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.Tenantable;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 授权
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUTH_PERMISSION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Permission extends BaseBusEntity implements Tenantable {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  /** 授权范围 */
  @Column(name = "SCOPE", length = 25, nullable = false)
  private String scope;

  /** 授权主体 */
  @Embedded private Grantee grantee;

  /** 授权策略 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "POLICY_ID",
      foreignKey = @ForeignKey(name = "FK_PERMISSION_POLICY_ID"),
      updatable = false,
      nullable = false)
  private PermissionPolicy policy;

  /** 租户ID */
  @Column(name = "TENANT_ID", length = 24, updatable = false)
  private String tenantId;
}
