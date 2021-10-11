package cn.asany.cms.article.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.lucene.annotations.Indexed;

import javax.persistence.*;
import java.util.List;

/**
 * 推荐位
 *
 * @author ChenWenJie
 * @date 2020/10/22 10:27 上午
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Indexed
@Entity
@Table(name = "CMS_FEATURE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Feature extends BaseBusEntity {
  @Id
  @Column(name = "ID", length = 10)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 编码 */
  @Column(name = "code")
  private String code;
  /** 名称 */
  @Column(name = "NAME", nullable = false, length = 150)
  private String name;
  /** 是否启用流程 true 启用，false 不启用 */
  @Column(name = "ENABLE_REVIEW")
  private Boolean needReview;
  /** 描述 */
  @Column(name = "description")
  private String description;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "feature", cascade = CascadeType.REMOVE)
  private List<ArticleFeature> articleFeatures;
}
