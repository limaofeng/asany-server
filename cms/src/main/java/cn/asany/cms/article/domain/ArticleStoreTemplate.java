package cn.asany.cms.article.domain;

import cn.asany.cms.content.domain.enums.ContentType;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 文章存储模版
 *
 * @author limaofeng
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CMS_ARTICLE_STORE_TEMPLATE")
public class ArticleStoreTemplate extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 内容类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "CONTENT_TYPE", length = 20)
  private ContentType contentType;
  /** 存储类 */
  @Column(name = "STORAGE_CLASS", length = 150)
  private String storageClass;
  /** 排序 */
  @Column(name = "SORT")
  private Integer index;
  /** 相关展示组件 */
  @Embedded private ArticleComponents components;
}
