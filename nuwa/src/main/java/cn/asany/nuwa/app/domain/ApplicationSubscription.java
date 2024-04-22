package cn.asany.nuwa.app.domain;

import cn.asany.nuwa.app.domain.enums.SubscriptionStatus;
import cn.asany.nuwa.app.domain.enums.SubscriptionType;
import cn.asany.organization.core.domain.Organization;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 应用会员
 *
 * @author limaofeng
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

  private SubscriptionType type;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    ApplicationSubscription that = (ApplicationSubscription) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
