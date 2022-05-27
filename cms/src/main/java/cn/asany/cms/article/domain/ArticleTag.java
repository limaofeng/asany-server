package cn.asany.cms.article.domain;

import cn.asany.cms.article.domain.converter.MetaDataConverter;
import cn.asany.organization.core.domain.Organization;
import cn.asany.security.core.domain.User;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.*;
import net.bytebuddy.description.modifier.Ownership;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.search.annotations.IndexEmbedBy;

/**
 * 文章 - 标签
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
    name = "CMS_ARTICLE_TAG",
    uniqueConstraints = @UniqueConstraint(name = "UK_ARTICLE_TAG_SLUG", columnNames = "SLUG"))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArticleTag extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 10)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 编码 */
  @Column(name = "SLUG", nullable = false, length = 100)
  private String slug;
  /** 路径 */
  @IndexEmbedBy(value = Article.class)
  @Column(name = "PATH", length = 500)
  private String path;
  /** 名称 */
  @Column(name = "NAME", nullable = false, length = 150)
  private String name;
  /** 封面 */
  @Column(name = "COVER", length = 500, columnDefinition = "JSON")
  @Convert(converter = FileObjectConverter.class)
  private FileObject cover;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 400)
  private String description;
  /** 排序字段 */
  @Column(name = "SORT")
  private Integer index;
  /** SEO 优化字段 */
  @Column(name = "META_DATA", length = 250, columnDefinition = "JSON")
  @Convert(converter = MetaDataConverter.class)
  private MetaData meta;
  /** 上级栏目 */
  @JsonProperty("parent_id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_ARTICLE_TAG_PARENT"))
  private ArticleTag parent;

  /** 下级栏目 */
  @OneToMany(
      mappedBy = "parent",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @OrderBy("index ASC")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<ArticleTag> children;

  /** 所有者 */
  @Any(
      metaColumn =
          @Column(name = "OWNERSHIP_TYPE", length = 10, insertable = false, updatable = false),
      fetch = FetchType.LAZY)
  @AnyMetaDef(
      idType = "long",
      metaType = "string",
      metaValues = {
        @MetaValue(targetEntity = User.class, value = User.OWNERSHIP_KEY),
        @MetaValue(targetEntity = Organization.class, value = Organization.OWNERSHIP_KEY)
      })
  @JoinColumn(name = "OWNERSHIP_ID", insertable = false, updatable = false)
  private Ownership ownership;
}
