package cn.asany.cms.article.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.lucene.annotations.Indexed;

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
@Table(name = "CMS_RECOMMEND")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Recommend extends BaseBusEntity {
  @Id
  @Column(name = "ID", length = 10)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "recommend_gen")
  @TableGenerator(
      name = "recommend_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "recommend_gen:id",
      valueColumnName = "gen_value")
  private Long id;
  /** 编码 */
  @Column(name = "code")
  private String code;
  /** 名称 */
  @Column(name = "NAME", nullable = false, length = 150)
  private String name;
  /** 是否启用流程 true 启用，false 不启用 */
  @Column(name = "ENABLE_PROCESS")
  private Boolean enableProcess;
  /** 描述 */
  @Column(name = "description")
  private String description;
  /** 组织机构 */
  @Column(name = "ORGANIZATION_ID", updatable = false, nullable = false)
  private String organization;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "recommend", cascade = CascadeType.REMOVE)
  private List<ArticleRecommend> articleRecommends;
}
