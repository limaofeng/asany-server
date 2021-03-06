package cn.asany.cms.article.domain;

import cn.asany.cms.article.domain.enums.ArticleBodyType;
import cn.asany.cms.article.domain.enums.ArticleStatus;
import cn.asany.cms.body.domain.Content;
import cn.asany.cms.permission.domain.Permission;
import cn.asany.organization.core.domain.Organization;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import cn.asany.storage.api.converter.FileObjectsConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import javax.validation.constraints.Null;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.search.annotations.IndexProperty;
import org.jfantasy.framework.search.annotations.Indexed;
import org.jfantasy.framework.spring.validation.Operation;

/**
 * 文章表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:47:20
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Indexed(indexName = "articles")
@Entity
@Table(
    name = "CMS_ARTICLE",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_ARTICLE_SLUG",
            columnNames = {"ORGANIZATION_ID", "SLUG"}))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "target", "comments"})
public class Article extends BaseBusEntity {

  @Id
  @Null(groups = Operation.Create.class)
  @Column(name = "ID", nullable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 文章编号，由于文章ID自增，有部分情况需要保证使用一个唯一，且有意义的标示符，定位到唯一的一篇文章 必须保证全局唯一 */
  @Column(name = "SLUG", length = 200)
  private String slug;
  /** 文章对应的 频道 / 栏目 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "CATEGORY_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_CATEGORY"))
  private ArticleCategory category;
  /** 状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20, nullable = false)
  private ArticleStatus status;
  /** 文章标题 */
  @IndexProperty(store = true)
  @Column(name = "TITLE")
  private String title;
  /** 摘要 */
  @IndexProperty(store = true)
  @Column(name = "SUMMARY", length = 500)
  private String summary;
  /** 文章图片 */
  @Column(name = "IMAGE", columnDefinition = "JSON")
  @Convert(converter = FileObjectConverter.class)
  private FileObject image;
  /** 附件 */
  @Column(name = "ATTACHMENTS", columnDefinition = "JSON")
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
  /** 文章对象的附加信息 */
  @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<ArticleMetaField> metafields;
  /** 发布日期 */
  @IndexProperty(store = true)
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "PUBLISHED_AT")
  private Date publishedAt;
  /** 正文类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STORE_TEMPLATE_ID", length = 25)
  private ArticleBodyType bodyType;
  /** 正文 ID */
  @Column(name = "BODY_ID")
  private Long bodyId;
  /** 文章正文 */
  @Any(
      metaColumn =
          @Column(name = "STORE_TEMPLATE_ID", length = 20, insertable = false, updatable = false),
      fetch = FetchType.LAZY)
  @AnyMetaDef(
      idType = "long",
      metaType = "string",
      metaValues = {@MetaValue(targetEntity = Content.class, value = Content.TYPE_KEY)})
  @JoinColumn(name = "BODY_ID", insertable = false, updatable = false)
  private ArticleBody body;
  /** 存储模版 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "STORE_TEMPLATE_ID",
      foreignKey = @ForeignKey(name = "FK_ARTICLE_STORE_TEMPLATE"),
      insertable = false,
      updatable = false)
  private ArticleStoreTemplate storeTemplate;
  /** 所属组织 */
  @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_ARTICLE_ORGANIZATION_ID"))
  @ToString.Exclude
  private Organization organization;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Article article = (Article) o;
    return id != null && Objects.equals(id, article.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
