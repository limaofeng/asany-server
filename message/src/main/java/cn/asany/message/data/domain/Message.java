package cn.asany.message.data.domain;

import cn.asany.message.data.domain.enums.MessageStatus;
import cn.asany.message.define.domain.MessageDefinition;
import cn.asany.message.define.domain.MessageType;
import cn.asany.security.core.domain.User;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
  @ManyToOne(targetEntity = MessageDefinition.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "TYPE", foreignKey = @ForeignKey(name = "FK_MESSAGE_TYPE"))
  @ToString.Exclude
  private MessageType type;
  /** 消息状态 */
  @Column(name = "STATUS", length = 10)
  @Enumerated(EnumType.STRING)
  private MessageStatus status;
  /** 消息内容 */
  @Column(name = "CONTENT", columnDefinition = "TEXT")
  private String content;
  /** 发送方 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "`FROM`",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_MESSAGE_FROM_UID"))
  private User from;
  /** 收件人 */
  @OneToMany(
      mappedBy = "message",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @ToString.Exclude
  private List<MessageRecipient> recipients;
}
