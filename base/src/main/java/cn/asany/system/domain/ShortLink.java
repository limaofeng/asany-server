package cn.asany.system.domain;

import cn.asany.base.common.domain.Owner;
import cn.asany.system.domain.enums.ShortLinkOwnerType;
import java.util.Date;
import java.util.Map;
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
  @ElementCollection(fetch = FetchType.EAGER)
  @MapKeyColumn(name = "NAME", length = 50)
  @Column(name = "VALUE", length = 2048)
  @CollectionTable(
      name = "SYS_SHORT_LINK_META_DATA",
      joinColumns =
          @JoinColumn(
              name = "SHORT_LINK_ID",
              referencedColumnName = "ID",
              foreignKey = @ForeignKey(name = "FK_SHORT_LINK_META_DATA_SHORT_LINK_ID")))
  private Map<String, String> metadata;
  /** 所有者 */
  @Embedded private Owner<ShortLinkOwnerType> owner;
}
