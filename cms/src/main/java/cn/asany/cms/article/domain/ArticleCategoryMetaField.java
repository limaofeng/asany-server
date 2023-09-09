package cn.asany.cms.article.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "CMS_ARTICLE_CATEGORY_META_FIELD",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_ARTICLE_CATEGORY_META_FIELD",
            columnNames = {"ARTICLE_CATEGORY_ID", "NAMESPACE", "META_KEY"}))
public class ArticleCategoryMetaField extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 字段 Key */
  @Column(name = "META_KEY", length = 50)
  private String key;

  /** 字段类型 */
  @Column(name = "TYPE", length = 50)
  private String type;

  /** 字段 namespace */
  @Column(name = "NAMESPACE", length = 50)
  private String namespace;

  /** 字段 值 */
  @Column(name = "META_VALUE", length = 250)
  private String value;

  /** 字段 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 关联文章 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ARTICLE_CATEGORY_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_ARTICLE_CATEGORY_META_FIELD_ARTICLE_ID"))
  private ArticleCategory category;
}
