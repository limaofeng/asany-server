package cn.asany.cms.article.domain;

import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "CMS_ARTICLE_META_FIELD_DEFINITION",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_ARTICLE_META_FIELD_DEFINITION",
            columnNames = {"ARTICLE_CATEGORY_ID", "NAMESPACE", "META_KEY"}))
public class ArticleMetafieldDefinition extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false)
  @TableGenerator
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

  /** 字段 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 关联文章 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ARTICLE_CATEGORY_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_META_FIELD_DEFINITION_ARTICLE_CATEGORY_ID"))
  private ArticleCategory category;
}
