package cn.asany.security.core.domain;

import cn.asany.security.core.domain.enums.UserStatusAvailability;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_USER_STATUS")
@JsonIgnoreProperties({
  "hibernateLazyInitializer",
  "handler",
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserStatus extends BaseBusEntity {

  @Id
  @Column(name = "USER_ID", nullable = false, updatable = false, precision = 22)
  @GenericGenerator(
      name = "UserStatusPkGenerator",
      strategy = "foreign",
      parameters = {@org.hibernate.annotations.Parameter(name = "property", value = "user")})
  @GeneratedValue(generator = "UserStatusPkGenerator")
  private Long id;
  /** 心情与状态 */
  @Column(name = "EMOJI")
  private String emoji;
  /** 消息 */
  @Column(name = "MESSAGE")
  private String message;
  /** 可用性 */
  @Enumerated(EnumType.STRING)
  @Column(name = "AVAILABILITY")
  private UserStatusAvailability availability;
  /** 过期时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "EXPIRES_AT")
  private Date expiresAt;
  /** 用户 */
  @OneToOne(fetch = FetchType.LAZY, targetEntity = User.class, mappedBy = "status")
  @ToString.Exclude
  private User user;
}
