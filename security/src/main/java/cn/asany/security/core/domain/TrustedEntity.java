package cn.asany.security.core.domain;

import javax.persistence.*;
import lombok.*;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class TrustedEntity {
  /** 可信实体类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TRUSTED_ENTITY_TYPE",
      foreignKey = @ForeignKey(name = "FK_ROLE_TRUSTED_ENTITY_TYPE"),
      updatable = false)
  @ToString.Exclude
  private TrustedEntityType trustedEntityType;
  /** 可信实体ID */
  @Column(name = "TRUSTED_ENTITY_ID", length = 32)
  private String TrustedEntityId;
}
