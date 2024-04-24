package cn.asany.security.core.domain;

import cn.asany.organization.core.domain.Organization;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.SnowflakeGenerator;

@Data
@Builder
@Entity
@Table(name = "SYS_TENANT")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tenant extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 32)
  @SnowflakeGenerator
  private String id;

  @Column(name = "DOMAIN", length = 32)
  private String domain;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ACCOUNT_ID",
      referencedColumnName = "ID",
      foreignKey = @ForeignKey(name = "FK_TENANT_ACCOUNT_ID"))
  private User mainAccount;

  @OneToOne(mappedBy = "tenant", fetch = FetchType.LAZY)
  private AccessControlSettings accessControlSettings;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DEFAULT_ORGANIZATION_ID", referencedColumnName = "ID", nullable = false)
  private Organization defaultOrganization;
}
