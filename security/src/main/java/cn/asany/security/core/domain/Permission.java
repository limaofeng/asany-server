package cn.asany.security.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.Tenantable;

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
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /**
   * 授权范围
   */
  @Column(name = "SCOPE", length = 25, nullable = false)
  private String scope;
  /**
   * 授权主体
   */
  @Column(name = "SUBJECT", length = 25, nullable = false)
  private String subject;
  /**
   * 授权策略
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "POLICY_ID",
      foreignKey = @ForeignKey(name = "FK_PERMISSION_POLICY_ID"),
    updatable = false,
      nullable = false)
  private PermissionPolicy policy;
  /**
   * 租户ID
   */
  @Column(name = "TENANT_ID", length = 24)
  private String tenantId;
}
