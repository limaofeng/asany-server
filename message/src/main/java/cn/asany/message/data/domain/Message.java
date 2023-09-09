package cn.asany.message.data.domain;

import cn.asany.message.data.domain.enums.MessageStatus;
import cn.asany.message.data.util.MessageUtils;
import cn.asany.message.define.domain.MessageType;
import cn.asany.message.define.domain.converter.MessageContentConverter;
import cn.asany.message.define.domain.toys.MessageContent;
import cn.asany.security.core.domain.User;
import jakarta.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.MapConverter;

/**
 * 消息
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "MSG_MESSAGE")
public class Message extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 消息类型 */
  @ManyToOne(targetEntity = MessageType.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "TYPE", foreignKey = @ForeignKey(name = "FK_MESSAGE_TYPE"))
  @ToString.Exclude
  private MessageType type;

  /** 消息状态 */
  @Column(name = "STATUS", length = 10)
  @Enumerated(EnumType.STRING)
  private MessageStatus status;

  @Column(name = "VARIABLES", nullable = false, columnDefinition = "JSON")
  @Convert(converter = MapConverter.class)
  private Map<String, Object> variables;

  /** 消息内容 */
  @Column(name = "CONTENT", columnDefinition = "TEXT")
  @Convert(converter = MessageContentConverter.class)
  private MessageContent content;

  /** 发送人 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SENDER_ID", foreignKey = @ForeignKey(name = "FK_MESSAGE_FROM_UID"))
  private User sender;

  /** 收件人 */
  @OneToMany(
      mappedBy = "message",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @ToString.Exclude
  private List<MessageRecipient> recipients;

  /** 失败原因 */
  @Column(name = "FAILURE_REASON", length = 200)
  private String failureReason;

  public static class MessageBuilder {
    public MessageBuilder recipients(List<MessageRecipient> recipients) {
      this.recipients = recipients;
      return this;
    }

    public MessageBuilder recipients(String... recipients) {
      this.recipients =
          Stream.of(recipients).map(MessageUtils::recipient).collect(Collectors.toList());
      return this;
    }

    public Message build() {
      Message message = new Message();

      recipients.forEach(r -> r.setMessage(message));

      message.setType(type);
      message.setVariables(variables);
      message.setContent(content);
      message.setSender(sender);
      message.setRecipients(recipients);
      message.setFailureReason(failureReason);
      message.setStatus(status);
      return message;
    }
  }
}
