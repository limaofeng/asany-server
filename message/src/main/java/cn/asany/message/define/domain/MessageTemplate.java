package cn.asany.message.define.domain;

import cn.asany.message.data.util.MessageUtils;
import cn.asany.message.define.domain.converter.MessageContentConverter;
import cn.asany.message.define.domain.converter.VariableDefinitionListConverter;
import cn.asany.message.define.domain.enums.TemplateType;
import cn.asany.message.define.domain.toys.MessageContent;
import cn.asany.message.define.domain.toys.VariableDefinition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import java.util.Map;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 消息模版
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "MSG_MESSAGE_TEMPLATE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MessageTemplate extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 模版类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", nullable = false, length = 10)
  private TemplateType type;

  /** 模版名称 */
  @Column(name = "NAME", nullable = false, length = 50)
  private String name;

  /** 签名 */
  @Column(name = "SIGN", nullable = false, length = 20)
  private String sign;

  /** 模版中使用的变量 */
  @Column(name = "VARIABLES", nullable = false, columnDefinition = "JSON")
  @Convert(converter = VariableDefinitionListConverter.class)
  private List<VariableDefinition> variables;

  /**
   * 模板号 type = SMS 时 CODE = cn.asany.sms.domain.Template 的 ID type = EMAIL 时 CODE =
   * cn.asany.mail.domain.Template 的 ID type = MS 时, 取 content 内容
   */
  @Column(name = "CODE", length = 50)
  private String code;

  /** 内容模版 */
  @Column(name = "CONTENT", columnDefinition = "Text")
  @Convert(converter = MessageContentConverter.class)
  private MessageContent content;

  public void validate(Map<String, Object> values) {
    MessageUtils.validate(this.variables, values);
  }
}
