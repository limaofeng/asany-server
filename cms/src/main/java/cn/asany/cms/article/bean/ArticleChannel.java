package cn.asany.cms.article.bean;

import cn.asany.cms.article.bean.converter.MetaDataConverter;
import cn.asany.cms.permission.bean.Permission;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.lucene.annotations.IndexEmbedBy;

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
    name = "CMS_ARTICLE_CHANNEL",
    uniqueConstraints = @UniqueConstraint(name = "UK_ARTICLE_CHANNEL_CODE", columnNames = "CODE"))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArticleChannel extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 10)
  private Long id;
  /** 编码 */
  @Column(name = "CODE", nullable = false, length = 100)
  private String code;
  /** 路径 */
  @IndexEmbedBy(value = Article.class)
  @Column(name = "PATH", length = 500)
  private String path;
  /** 名称 */
  @Column(name = "NAME", nullable = false, length = 150)
  private String name;
  /** 封面 */
  @Column(name = "COVER", length = 500, columnDefinition = "json")
  @Convert(converter = FileObjectConverter.class)
  private FileObject cover;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 400)
  private String description;
  /** 排序字段 */
  @Column(name = "SORT")
  private Integer sort;
  /** SEO 优化字段 */
  @Column(name = "META_DATA", length = 250, columnDefinition = "json")
  @Convert(converter = MetaDataConverter.class)
  private MetaData meta;
  /** 组织机构 */
  @Column(name = "ORGANIZATION_ID", updatable = false, nullable = false)
  private Long organization;
  /** 上级栏目 */
  @JsonProperty("parent_id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_ARTICLE_CHANNEL_PARENT"))
  private ArticleChannel parent;

  /** 下级栏目 */
  @OneToMany(
      mappedBy = "parent",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @OrderBy("sort ASC")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<ArticleChannel> children;

  @Transient private List<Permission> permissions;
}
