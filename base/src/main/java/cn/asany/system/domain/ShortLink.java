package cn.asany.system.domain;

import cn.asany.base.common.domain.Owner;
import cn.asany.system.domain.enums.ShortLinkOwnerType;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Entity
@Table(name = "SYS_SHORT_LINK")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShortLink extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 短链接标识符 */
  @Column(name = "CODE", nullable = false, unique = true, length = 10)
  private String code;
  /** 原始链接 */
  @Column(name = "URL", length = 2048)
  private String url;
  /** 过期时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "EXPIRES_AT")
  private Date expiresAt;
  /** 分类 */
  @Column(name = "CATEGORY", length = 50, nullable = false)
  private String category;
  /** 访问次数 */
  @Builder.Default
  @Column(name = "ACCESS_COUNT", nullable = false)
  private Long accessCount = 0L;
  /** 元数据 */
  @OneToMany(mappedBy = "shortLink", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ShortLinkMetaData> metaDatas;
  /** 所有者 */
  @Embedded private Owner<ShortLinkOwnerType> owner;
}
