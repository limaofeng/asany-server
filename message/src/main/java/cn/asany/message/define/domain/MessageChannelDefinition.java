package cn.asany.message.define.domain;

import cn.asany.message.api.IChannelConfig;
import cn.asany.message.api.MSChannelConfig;
import cn.asany.message.api.SMSChannelConfig;
import cn.asany.message.define.domain.enums.TemplateType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

/**
 * 消息通道定义
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "MSG_MESSAGE_CHANNEL_DEFINITION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MessageChannelDefinition extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Column(name = "NAME", nullable = false, length = 20)
  private String name;

  /** 是否为系统内置 */
  @Builder.Default
  @Column(name = "IS_SYSTEM", updatable = false, length = 1)
  private Boolean system = false;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 10, nullable = false, updatable = false)
  private TemplateType type;

  @Column(name = "CONFIG_STORE", columnDefinition = "JSON")
  private String config;

  @Transient
  public IChannelConfig getChannelConfig() {
    if (type == TemplateType.SMS) {
      return JSON.deserialize(config, SMSChannelConfig.class);
    } else if (type == TemplateType.EMAIL) {
      return JSON.deserialize(config, SMSChannelConfig.class);
    } else if (type == TemplateType.MS) {
      return JSON.deserialize(config, MSChannelConfig.class);
    }
    return null;
  }
}
