package cn.asany.cms.article.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 文章推荐位
 *
 * @author limaofeng
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CMS_ARTICLE_FEATURE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ArticleFeature extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 10)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 编码 */
  @Column(name = "CODE")
  private String code;
  /** 名称 */
  @Column(name = "NAME", nullable = false, length = 150)
  private String name;
  /** 是否启用流程 true 启用，false 不启用 */
  @Column(name = "ENABLE_REVIEW")
  private Boolean needReview;
  /** 描述 */
  @Column(name = "DESCRIPTION")
  private String description;
}
