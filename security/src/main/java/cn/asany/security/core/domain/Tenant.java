package cn.asany.security.core.domain;

import cn.asany.organization.core.domain.Organization;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Builder
@Entity
@Table(name = "SYS_TENANT")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tenant extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "snowflake")
  @GenericGenerator(
      name = "snowflake",
      strategy = "snowflake",
      parameters = {
        @Parameter(name = "workerId", value = "1"),
        @Parameter(name = "dataCenterId", value = "1")
      })
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
