package cn.asany.nuwa.app.bean;

import cn.asany.nuwa.app.bean.enums.SubscriptionStatus;
import cn.asany.organization.core.bean.Organization;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 应用会员
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(
    name = "NUWA_APPLICATION_SUBSCRIPTION",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_APPLICATION_SUBSCRIPTION_ORG",
          columnNames = {"APPLICATION_ID", "ORGANIZATION_ID"}),
    })
public class ApplicationSubscription extends BaseBusEntity {
  /** ID */
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "APPLICATION_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_SUBSCRIPTION_APP"))
  private Application application;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 50)
  private SubscriptionStatus status;

  /** 所有者 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_SUBSCRIPTION_ORG"))
  private Organization membership;
}
