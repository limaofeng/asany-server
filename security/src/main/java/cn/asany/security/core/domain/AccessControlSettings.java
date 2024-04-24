package cn.asany.security.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.Tenantable;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 访问控制
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUTH_ACCESS_CONTROL_SETTINGS")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AccessControlSettings extends BaseBusEntity implements Tenantable {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  /** 密码规则 */
  @Embedded private PasswordPolicy passwordPolicy;

  //  /** 用户安全设置 */
  //  @Embedded private UserSecuritySettings userSecuritySettings;
  /** 租户ID */
  @Column(name = "TENANT_ID", length = 32, nullable = false, updatable = false)
  private String tenantId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TENANT_ID",
      foreignKey = @ForeignKey(name = "FK_ACCESS_CONTROL_SETTINGS_TENANT_ID"),
      insertable = false,
      updatable = false,
      nullable = false)
  private Tenant tenant;
}
