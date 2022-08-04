package cn.asany.pm.rule.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ISSUE_MESSAGE_RULE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IssueMessageRule {
  /** 范围ID */
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 消息规则名称 */
  @Column(name = "NAME")
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION")
  private String description;
  /** 消息规则内容 */
  @Column(name = "CONTENT")
  private String content;
  /** 对应的操作 */
  @Column(name = "OPERATION")
  private String operation;
  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
  /** 是否多次提醒 */
  @Column(name = "MESSAGE_LOOP")
  private Boolean messageLoop;
  /** 消息间隔时长 */
  @Column(name = "TIME")
  private Long time;
  /** 提醒人员类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "MESSAGE_RULE", length = 20)
  private MessageRuleEum messageRuleEum;
}
