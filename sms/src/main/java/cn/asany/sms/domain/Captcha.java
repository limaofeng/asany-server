package cn.asany.sms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 短信验证码
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-4 下午01:56:29
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "SMS_CAPTCHA")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "config"})
public class Captcha extends BaseBusEntity {

  @Id
  @GeneratedValue(generator = "hibernate-uuid")
  @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
  @Column(name = "ID", nullable = false, updatable = false, length = 32)
  private String id;
  /** 配置信息 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CONFIG_ID", foreignKey = @ForeignKey(name = "FK_CONFIG_CAPTCHA"))
  private CaptchaConfig config;
  /** 绑定的会话ID */
  @Column(name = "SESSION_ID", length = 120, nullable = false)
  private String sessionId;
  /** 生成的CODE值 */
  @Column(name = "VALUE", length = 120)
  private String value;
  /** 手机号 */
  @Column(name = "PHONE", length = 120, nullable = false)
  private String phone;
  /** 已经重试的次数 */
  @Column(name = "RETRY")
  private int retry;
}
