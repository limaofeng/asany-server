package cn.asany.nuwa.app.bean;

import cn.asany.security.oauth.bean.AccessToken;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 客户端凭证
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"accessTokens"})
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
  /** 密钥 */
  @Column(name = "SECRET", length = 40, updatable = false)
  private String secret;
  /** 客户端 */
  @Column(name = "CLIENT_ID", length = 20, updatable = false, nullable = false)
  private String client;
  /** 访问令牌 */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @OrderBy(" createdAt desc ")
  @JoinColumn(name = "CLIENT_ID", referencedColumnName = "CLIENT_ID", updatable = false)
  private List<AccessToken> accessTokens;
}
