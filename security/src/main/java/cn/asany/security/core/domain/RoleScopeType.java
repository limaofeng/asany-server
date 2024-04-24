package cn.asany.security.core.domain;

import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.Tenantable;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/** 角色使用范围类型 如： 部门，会议 等 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_ROLE_SCOPE_TYPE")
public class RoleScopeType extends BaseBusEntity implements Tenantable {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  /** 服务 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "SERVICE",
      foreignKey = @ForeignKey(name = "FK_ROLE_SCOPE_TYPE_SERVICE_ID"),
      updatable = false,
      nullable = false)
  private AuthorizedService service;

  /** 租户ID */
  @Column(name = "TENANT_ID", length = 24)
  private String tenantId;
}
