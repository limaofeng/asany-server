package cn.asany.pm.rule.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ISSUE_INTEGRAL_RULE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IssueIntegralRule extends BaseBusEntity {
  /** 范围ID */
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 条件对应ID */
  @Column(name = "CONDITION_ID")
  private Long conditionId;

  /** 分级类别 */
  @Enumerated(EnumType.STRING)
  @Column(name = "CODE", length = 20)
  private IssueIntegralRuleEnum code;

  /** 分级名称 */
  @Column(name = "NAME")
  private String name;

  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;

  /** 分值 */
  @Column(name = "SCORE")
  private Integer score;
}
