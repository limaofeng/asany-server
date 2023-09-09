package cn.asany.sms.domain;

import cn.asany.base.sms.SMSProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 短信供应商
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "SmsProvider")
@Table(name = "SMS_PROVIDER")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Provider extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false)
  private String id;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 10, nullable = false, updatable = false)
  private SMSProvider type;

  @Column(name = "NAME", nullable = false, length = 20)
  private String name;

  @Column(name = "DESCRIPTION", nullable = false, length = 500)
  private String description;

  @Column(name = "CONFIG_STORE", columnDefinition = "JSON")
  private String config;
}
