package cn.asany.cms.article.domain;

import cn.asany.organization.core.domain.Organization;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.search.annotations.IndexEmbedBy;

/**
 * 文章 - 频道 / 栏目
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
    name = "CMS_ARTICLE_CATEGORY",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_ARTICLE_CHANNEL_SLUG",
            columnNames = {"ORGANIZATION_ID", "SLUG"}))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArticleCategory extends BaseBusEntity {

  public static final String SEPARATOR = "/";

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 编码 */
  @Column(name = "SLUG", length = 100)
  private String slug;
  /** 路径 */
  @IndexEmbedBy(value = Article.class)
  @Column(name = "PATH", length = 500)
  private String path;
  /** 名称 */
  @Column(name = "NAME", nullable = false, length = 150)
  private String name;
  /** 对应的图标 */
  @Column(name = "ICON", length = 120)
  private String icon;
  /** 封面 */
  @Column(name = "IMAGE", length = 500, columnDefinition = "JSON")
  @Convert(converter = FileObjectConverter.class)
  private FileObject image;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 400)
  private String description;
  /** 对应的文章 */
  @OneToMany(
      mappedBy = "category",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<Article> articles;
  /** 排序字段 */
  @Column(name = "SORT")
  private Integer index;
  /** 层级 */
  @Column(name = "LEVEL")
  private Integer level;
  /** SEO 优化字段 */
  @Embedded private ArticleMetadata metadata;
  /** 上级栏目 */
  @JsonProperty("parent_id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_ARTICLE_CHANNEL_PARENT"))
  private ArticleCategory parent;
  /** 存储模版 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "STORE_TEMPLATE_ID",
      foreignKey = @ForeignKey(name = "FK_ARTICLE_CHANNEL_STORE_TEMPLATE"))
  private ArticleStoreTemplate storeTemplate;
  /** 下级栏目 */
  @OneToMany(
      mappedBy = "parent",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @OrderBy("index ASC")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<ArticleCategory> children;
  /** 所属组织 */
  @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_ARTICLE_CHANNEL_ORGANIZATION_ID"))
  @ToString.Exclude
  private Organization organization;
}
