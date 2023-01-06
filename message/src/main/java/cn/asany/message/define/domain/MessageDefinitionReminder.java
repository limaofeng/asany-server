package cn.asany.message.define.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.MapConverter;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "MSG_MESSAGE_DEFINITION_REMINDER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MessageDefinitionReminder extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "MESSAGE_DEFINITION_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_MSG_REMINDER_MESSAGE_DEFINITION_ID"))
  private MessageDefinition messageDefinition;
  /** 与模版之间的变量映射表 */
  @Convert(converter = MapConverter.class)
  @Column(name = "MAPPING_VARIABLES", columnDefinition = "Text")
  private Map<String, String> mappingVariables;
  /** 提醒定义 */
  @ManyToOne(targetEntity = MessageDefinition.class, fetch = FetchType.LAZY)
  @JoinColumn(
      name = "REMINDER_DEFINITION_ID",
      foreignKey = @ForeignKey(name = "FK_MSG_REMINDER_REMINDER_DEFINITION_ID"))
  @ToString.Exclude
  private ReminderDefinition reminderDefinition;
}
