package cn.asany.cms.article.bean;

import cn.asany.cms.article.bean.converter.MetaDataConverter;
import cn.asany.cms.article.bean.enums.ArticleCategory;
import cn.asany.cms.article.bean.enums.ArticleStatus;
import cn.asany.cms.article.bean.enums.ArticleType;
import cn.asany.cms.permission.bean.Permission;
import cn.asany.organization.core.bean.Organization;
import cn.asany.security.core.bean.User;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import cn.asany.storage.api.converter.FileObjectsConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import javax.validation.constraints.Null;
import lombok.*;
import net.bytebuddy.description.modifier.Ownership;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.lucene.annotations.IndexProperty;
import org.jfantasy.framework.lucene.annotations.Indexed;
import org.jfantasy.framework.spring.validation.Operation;

/**
 * 文章表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:47:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Indexed
@Entity
@Table(
    name = "CMS_ARTICLE",
    uniqueConstraints = @UniqueConstraint(name = "UK_ARTICLE_SLUG", columnNames = "SLUG"))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "target", "comments"})
public class Article extends BaseBusEntity {

  @Id
  @Null(groups = Operation.Create.class)
  @Column(name = "ID", nullable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 文章编号，由于文章ID自增，有部分情况需要保证使用一个唯一，且有意义的标示符，定位到唯一的一篇文章 必须保证全局唯一 */
  @Column(name = "SLUG", length = 200)
  private String slug;
  /** 文章对应的 频道 / 栏目 */
  @ManyToMany(targetEntity = ArticleChannel.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "CMS_ARTICLE_CHANNEL_ITEM",
      joinColumns =
          @JoinColumn(
              name = "ARTICLE_ID",
              foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_CHANNEL_ITEM_AID")),
      inverseJoinColumns =
          @JoinColumn(
              name = "CHANNEL_ID",
              foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_CHANNEL_ITEM_CID")))
  private List<ArticleChannel> channels;
  /** 类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20)
  private ArticleType type;
  /** 状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20, nullable = false)
  private ArticleStatus status;
  /** 文章标题 */
  @IndexProperty(analyze = true, store = true)
  @Column(name = "TITLE")
  private String title;
  /** 摘要 */
  @IndexProperty(analyze = true, store = true)
  @Column(name = "SUMMARY", length = 500)
  private String summary;
  /** 文章封面 */
  @Column(name = "COVER_IMAGE", length = 500)
  @Convert(converter = FileObjectConverter.class)
  private FileObject cover;
  /** 附件 */
  @Column(name = "ATTACHMENTS", columnDefinition = "json")
  @Convert(converter = FileObjectsConverter.class)
  private List<FileObject> attachments;
  /** 作者 */
  @OneToMany(
      mappedBy = "article",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<ArticleAuthor> authors;
  /** 标签 */
  @ManyToMany(targetEntity = ArticleTag.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "CMS_ARTICLE_TAG_ITEM",
      joinColumns =
          @JoinColumn(
              name = "ARTICLE_ID",
              foreignKey = @ForeignKey(name = "FK_ARTICLE_TAG_ITEM_ARTICLE")),
      inverseJoinColumns =
          @JoinColumn(name = "TAG_ID", foreignKey = @ForeignKey(name = "FK_ARTICLE_TAG_ITEM_TAG")))
  private List<ArticleTag> tags;
  /** 推荐位 */
  @ManyToMany(targetEntity = ArticleFeature.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "CMS_ARTICLE_FEATURE_ITEM",
      joinColumns =
          @JoinColumn(
              name = "ARTICLE_ID",
              foreignKey = @ForeignKey(name = "FK_ARTICLE_FEATURE_ITEM_ARTICLE")),
      inverseJoinColumns =
          @JoinColumn(
              name = "FEATURE_ID",
              foreignKey = @ForeignKey(name = "FK_ARTICLE_FEATURE_ITEM_FID")))
  private List<ArticleFeature> features;
  /** SEO 优化字段 */
  @Column(name = "META_DATA", length = 250)
  @Convert(converter = MetaDataConverter.class)
  private MetaData meta;
  /** 发布日期 */
  @IndexProperty(store = true)
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "RELEASE_DATE")
  private Date publishedAt;
  /** 文章类别 */
  @Enumerated(EnumType.STRING)
  @Column(name = "CATEGORY", nullable = false, length = 25)
  private ArticleCategory category;

  /** 文章正文 */
  @Any(
      metaColumn = @Column(name = "CONTENT_TYPE", length = 25, updatable = false),
      fetch = FetchType.LAZY)
  @AnyMetaDef(
      idType = "long",
      metaType = "string",
      metaValues = {
        @MetaValue(targetEntity = HtmlContent.class, value = HtmlContent.TYPE_KEY),
        @MetaValue(targetEntity = MarkdownContent.class, value = MarkdownContent.TYPE_KEY)
      })
  @JoinColumn(name = "CONTENT_ID", updatable = false)
  private Content content;

  /** 所有者 */
  @Any(
      metaColumn = @Column(name = "OWNERSHIP_TYPE", length = 10, updatable = false),
      fetch = FetchType.LAZY)
  @AnyMetaDef(
      idType = "long",
      metaType = "string",
      metaValues = {
        @MetaValue(targetEntity = User.class, value = User.OWNERSHIP_KEY),
        @MetaValue(targetEntity = Organization.class, value = Organization.OWNERSHIP_KEY)
      })
  @JoinColumn(name = "OWNERSHIP_ID", updatable = false)
  private Ownership ownership;

  /** 最后评论时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "LAST_COMMENT_TIME")
  @CreationTimestamp
  private Date lastCommentTime;
  /** 有效期限 true 永久 false 短期 */
  @Column(name = "VALIDITY")
  private Boolean validity;
  /** 有效期限 开始时间 */
  @Column(name = "VALIDITY_START_DATE")
  private Date validityStartDate;
  /** 有效期限 结束时间 */
  @Column(name = "VALIDITY_END_DATE")
  private Date validityEndDate;

  @Transient private List<Permission> permissions;
}