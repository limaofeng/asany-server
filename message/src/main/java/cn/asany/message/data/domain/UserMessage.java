package cn.asany.message.data.domain;

import cn.asany.message.define.domain.MessageType;
import cn.asany.security.core.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 用户收到的消息
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "MSG_USER_MESSAGE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserMessage extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @ManyToOne(targetEntity = MessageType.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "TYPE", foreignKey = @ForeignKey(name = "FK_USER_MESSAGE_TYPE"))
  @ToString.Exclude
  private MessageType type;
  /** 资源标识符 */
  @Column(name = "URI", length = 150, updatable = false)
  private String uri;
  /** 原始消息 */
  @ManyToOne(targetEntity = Message.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "MESSAGE_ID", foreignKey = @ForeignKey(name = "FK_USER_MESSAGE_MID"))
  @ToString.Exclude
  private Message message;
  /** 已读标识 */
  @Builder.Default
  @Column(name = "`READ`", nullable = false)
  private Boolean read = false;
  /** 用户 */
  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "USER", foreignKey = @ForeignKey(name = "FK_USER_MESSAGE_UID"))
  private User user;
}
