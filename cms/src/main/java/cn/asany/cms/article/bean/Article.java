package cn.asany.cms.article.bean;

import cn.asany.cms.article.bean.converter.MetaDataConverter;
import cn.asany.cms.article.bean.databind.ContentDeserializer;
import cn.asany.cms.article.bean.enums.ArticleCategory;
import cn.asany.cms.article.bean.enums.ArticleStatus;
import cn.asany.cms.article.bean.enums.ArticleType;
import cn.asany.cms.permission.bean.Permission;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import cn.asany.storage.api.converter.FileObjectsConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.lucene.annotations.IndexProperty;
import org.jfantasy.framework.lucene.annotations.Indexed;
import org.jfantasy.framework.spring.validation.RESTful;

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
    uniqueConstraints = @UniqueConstraint(name = "UK_ARTICLE_URL", columnNames = "SN"))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "target", "comments"})
public class Article extends BaseBusEntity {

  private static final long serialVersionUID = 3480217915594201004L;

  @Id
  @Null(groups = RESTful.POST.class)
  @Column(name = "ID", nullable = false, precision = 22)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "article_gen")
  @TableGenerator(
      name = "article_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "cms_article:id",
      valueColumnName = "gen_value")
  private Long id;
  /** 文章编号，由于文章ID自增，有部分情况需要保证使用一个唯一，且有意义的标示符，定位到唯一的一篇文章 必须保证全局唯一 */
  @Column(name = "SN", length = 200)
  private String url;
  /** 文章对应的 频道 / 栏目 */
  @ManyToMany(targetEntity = ArticleTag.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "CMS_ARTICLE_CHANNEL",
      joinColumns =
          @JoinColumn(
              name = "ARTICLE_ID",
              foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_CHANNEL_AID")),
      inverseJoinColumns =
          @JoinColumn(
              name = "CHANNEL_ID",
              foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_CHANNEL_CID")))
  private List<ArticleTag> channels;
  /** 类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20)
  private ArticleType type;
  /** 状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20, nullable = false)
  private ArticleStatus status;
  /** 文章标题 */
  @NotEmpty(groups = RESTful.POST.class)
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
  @Column(name = "AUTHOR", length = 50)
  private String author;
  /** 标签 */
  @ManyToMany(targetEntity = ArticleTag.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "CMS_ARTICLE_TAGS",
      joinColumns =
          @JoinColumn(
              name = "ARTICLE_ID",
              foreignKey = @ForeignKey(name = "FK_ARTICLE_TAGS_ARTICLE")),
      inverseJoinColumns =
          @JoinColumn(name = "TAG_ID", foreignKey = @ForeignKey(name = "FK_ARTICLE_TAGS_TAG")))
  private List<ArticleTag> tags;
  /** 推荐位 */
  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "article",
      cascade = {CascadeType.REMOVE})
  private List<ArticleRecommend> recommend;
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
  @IndexProperty(analyze = true, store = true)
  @JoinColumn(name = "CONTENT_ID", foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_CONTENT"))
  @JsonDeserialize(using = ContentDeserializer.class)
  @OneToOne(
      targetEntity = Content.class,
      fetch = FetchType.LAZY,
      cascade = {CascadeType.ALL, CascadeType.REMOVE})
  private Content content;
  /** 组织机构 */
  @Column(name = "ORGANIZATION_ID", updatable = false, nullable = false)
  private Long organization;
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
  /** 标志位 flag 1：删除 0：未删除 */
  @Column(name = "flag")
  private boolean flag;

  @Transient private List<Permission> permissions;
}
