package cn.asany.message.define.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 消息类型
 *
 * @author
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "MSG_MESSAGE_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MessageType extends BaseBusEntity {
  /** 消息类型编码 ( notification / reminder / general ) */
  @Id
  @Column(name = "ID", nullable = false, updatable = false, length = 32)
  private String id;
  /** 消息类型名称 */
  @Column(name = "NAME", nullable = false, length = 150)
  private String name;
  /** 消息类型图标 */
  @Column(name = "ICON", nullable = false, length = 32)
  private String icon;
  /** 消息类型描述 */
  @Column(name = "DESCRIPTION", length = 400)
  private String description;

  /** 上级类型 */
  @JsonProperty("parent_id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_MESSAGE_TYPE_PARENT"))
  private MessageType parent;

  /** 下级类型 */
  @OneToMany(
      mappedBy = "parent",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<MessageType> children;

  /** 是否为系统内置 */
  @Builder.Default
  @Column(name = "IS_SYSTEM", updatable = false, length = 1)
  private Boolean system = false;

  /** 消息定义(包含模版 / 以及通知等设置) */
  @ManyToOne(targetEntity = MessageDefinition.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "DEFINITION_ID", foreignKey = @ForeignKey(name = "FK_MESSAGE_DEFINITION_ID"))
  @ToString.Exclude
  private MessageDefinition definition;
}
