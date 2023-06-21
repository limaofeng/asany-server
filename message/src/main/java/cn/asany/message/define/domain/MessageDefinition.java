package cn.asany.message.define.domain;

import cn.asany.message.data.utils.MessageUtils;
import cn.asany.message.define.domain.converter.VariableDefinitionListConverter;
import cn.asany.message.define.domain.toys.VariableDefinition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.MapConverter;

/**
 * 消息定义
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "MSG_MESSAGE_DEFINITION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MessageDefinition extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", nullable = false, length = 20)
  private String name;
  /** 是否为系统内置 */
  @Builder.Default
  @Column(name = "IS_SYSTEM", updatable = false, length = 1)
  private Boolean system = false;
  /** 消息变量 */
  @Column(name = "VARIABLES", nullable = false, columnDefinition = "JSON")
  @Convert(converter = VariableDefinitionListConverter.class)
  private List<VariableDefinition> variables;
  /** 与模版之间的变量映射表 (msgDataKey:dataKey) */
  @Convert(converter = MapConverter.class)
  @Column(name = "MAPPING_VARIABLES", columnDefinition = "Text")
  private Map<String, String> mappingVariables;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "SENDER",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_MESSAGE_DEFINITION_SENDER"))
  private MessageSenderDefinition sender;
  /** 模版文件 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TEMPLATE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_MESSAGE_DEFINITION_TEMPLATE_ID"))
  private MessageTemplate template;
  /** 提醒设置 - 额外的第三方提醒,比如: 短信 / 邮件 / APP_PUSH / WEB_PUSH */
  @OneToMany(
      mappedBy = "messageDefinition",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<MessageReminderDefinition> reminders;

  public void validate(Map<String, Object> values) {
    MessageUtils.validate(this.variables, values);
  }

  public Map<String, Object> toTemplateData(Map<String, Object> values) {
    Map<String, Object> newValues = new HashMap<>(values);
    Map<String, String> data = this.getMappingVariables();
    data.forEach(
        (key, value) -> {
          if (newValues.containsKey(key)) {
            newValues.remove(key);
            newValues.put(value, newValues.get(key));
          }
        });
    return newValues;
  }
}
