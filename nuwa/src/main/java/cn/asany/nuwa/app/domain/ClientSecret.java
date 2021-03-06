package cn.asany.nuwa.app.domain;

import cn.asany.security.oauth.domain.AccessToken;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.security.oauth2.core.ClientSecretType;

/**
 * 客户端凭证
 *
 * @author limaofeng
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "AUTH_CLIENT_SECRET",
    uniqueConstraints = {@UniqueConstraint(name = "UK_CLIENT_ID", columnNames = "CLIENT_ID")})
public class ClientSecret extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 密钥类型 */
  @Column(name = "TYPE", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private ClientSecretType type;
  /** 密钥 */
  @Column(name = "SECRET", length = 40, updatable = false)
  private String secret;
  /** 客户端 */
  @Column(name = "CLIENT_ID", length = 20, updatable = false, nullable = false)
  private String client;
  /** 访问令牌 */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @OrderBy("createdAt desc")
  @JoinColumn(name = "CLIENT_SECRET", referencedColumnName = "SECRET", updatable = false)
  @ToString.Exclude
  private List<AccessToken> accessTokens;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    ClientSecret that = (ClientSecret) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
