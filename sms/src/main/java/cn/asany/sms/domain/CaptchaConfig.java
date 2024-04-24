package cn.asany.sms.domain;

import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 短信验证码定制方案
 *
 * @author 李茂峰
 * @since 2013-7-4 上午11:24:41
 * @version 1.0
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "SMS_CAPTCHA_CONFIG")
public class CaptchaConfig extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, length = 32)
  private String id;

  /** 配置名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 生成验证码的长度 */
  @Column(name = "WORD_LENGTH", length = 2)
  @Builder.Default
  private int wordLength = 6;

  /** 验证码随机字符串 */
  @Column(name = "RANDOM_WORD", length = 50)
  @Builder.Default
  private String randomWord = "0123456789";

  /** 验证码验证失效时间 */
  @Builder.Default
  @Column(name = "EXPIRES", length = 8)
  private int expires = 5 * 60;

  /** 验证码重复生成时间 */
  @Column(name = "ACTIVE", length = 8)
  @Builder.Default
  private int active = 60;

  /** 验证码验证重试次数 */
  @Column(name = "RETRY", length = 8)
  @Builder.Default
  private int retry = 3;

  /** 模板名称 */
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(
      name = "TEMPLATE_ID",
      foreignKey = @ForeignKey(name = "FK_CAPTCHA_CONFIG_TEMPLATE_ID"))
  private Template template;
}
