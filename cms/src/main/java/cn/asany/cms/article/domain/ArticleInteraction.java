package cn.asany.cms.article.domain;

import cn.asany.cms.article.domain.enums.InteractionType;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ARTICLE_INTERACTIONS")
public class ArticleInteraction {
  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 交互类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", nullable = false)
  private InteractionType type;
  /** 对于注册用户 */
  @Column(name = "USER_ID")
  private Long userId;
  /** 对于匿名用户 */
  @Column(name = "SESSION_ID", length = 120)
  private String sessionId;
  /** 时间戳 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "TIMESTAMP", nullable = false)
  private Date timestamp;
  /** 阅读时长, 只在 READS 时有效 */
  @Column(name = "READING_TIME")
  private Long readingTime;
  /** 文章 */
  @ManyToOne
  @JoinColumn(
      name = "ARTICLE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_ARTICLE_INTERACTIONS_ARTICLE"))
  private Article article;
}
