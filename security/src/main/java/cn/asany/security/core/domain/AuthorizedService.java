package cn.asany.security.core.domain;

import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 服务
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "AuthService")
@Table(name = "AUTH_SERVICE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuthorizedService extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, length = 50)
  private String id;

  @Column(name = "NAME", length = 50)
  private String name;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;
}
