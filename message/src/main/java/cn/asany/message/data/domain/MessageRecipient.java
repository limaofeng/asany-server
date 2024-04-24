package cn.asany.message.data.domain;

import cn.asany.message.data.domain.enums.MessageRecipientType;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 消息收件人
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "MSG_MESSAGE_RECIPIENT")
public class MessageRecipient extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 收件人类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", nullable = false, length = 20)
  private MessageRecipientType type;

  /** 收件人值 */
  @Column(name = "VALUE", nullable = false, length = 50)
  private String value;

  /** 消息 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "MESSAGE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_MESSAGE_RECIPIENT_MID"))
  private Message message;

  public String formatString() {
    return this.type.getName() + MessageRecipientType.DELIMITER + this.value;
  }
}
