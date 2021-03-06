package cn.asany.cms.article.domain;

import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CMS_ARTICLE_STORE_TEMPLATE")
public class ArticleStoreTemplate extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, length = 20)
  private String id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 存储类 */
  @Column(name = "STORAGE_CLASS", length = 150)
  private String storageClass;
  /** 排序 */
  @Column(name = "SORT")
  private Integer index;
  /** 相关展示组件 */
  @Embedded private Components components;
}
