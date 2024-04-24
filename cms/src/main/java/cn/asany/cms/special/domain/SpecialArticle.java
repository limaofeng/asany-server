package cn.asany.cms.special.domain;

import cn.asany.base.usertype.FileUserType;
import cn.asany.cms.article.domain.Article;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import javax.tools.FileObject;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CMS_SPECIAL_ARTICLE")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "special", "article"})
public class SpecialArticle extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false)
  @TableGenerator
  private Long id;

  /** 期数 */
  @Column(name = "PERIODICAL", nullable = false)
  private String periodical;

  /** 标题 */
  @Column(name = "TITLE", nullable = false)
  private String title;

  /** 摘要 */
  @Column(name = "SUMMARY", length = 500, nullable = false)
  private String summary;

  /** 封面 */
  @Column(name = "COVER", length = 500)
  @Type(FileUserType.class)
  private FileObject cover;

  /** 发布日期 */
  @Column(name = "PUBLISH_DATE", length = 15, nullable = false)
  private String publishDate;

  /** 对应的文章 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "SPECIAL_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_SPECIALARTICLE_SPECIAL"))
  private Special special;

  /** 对应的文章 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ARTICLE_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_SPECIALARTICLE_ARTICLE"))
  private Article article;
}
