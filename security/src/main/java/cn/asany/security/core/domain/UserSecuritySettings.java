package cn.asany.security.core.domain;

import cn.asany.security.core.domain.enums.MultiFactorAuthentication;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 用户安全设置对象，用于存储和管理用户的安全设置。 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSecuritySettings {

  /* 保存 MFA 验证状态 7 天 */
  @Column(name = "STORE_MFA_STATUS_FOR_7_DAYS")
  private Boolean storeMFAStatusFor7Days;

  /* 是否允许自主管理密码 */
  @Column(name = "ALLOW_SELF_MANAGED_PASSWORD")
  private Boolean allowSelfManagedPassword;

  /* 是否允许自主管理 AccessKey */
  @Column(name = "ALLOW_SELF_MANAGED_ACCESS_KEY")
  private Boolean allowSelfManagedAccessKey;

  /* 是否允许自主管理 MFA 设备 */
  @Column(name = "ALLOW_SELF_MANAGED_MFA_DEVICE")
  private Boolean allowSelfManagedMFADevice;

  /* 登录时必须使用 MFA 的模式 */
  @Column(name = "REQUIRED_LOGIN_MFA_MODE")
  @Enumerated(EnumType.STRING)
  private MFAMode requiredLoginMFAMode;

  /* 多因素认证方式 */
  @Column(name = "LOGIN_MFA_MODE")
  @Enumerated(EnumType.STRING)
  private MultiFactorAuthentication loginMFAMode;

  /* 是否允许钉钉 */
  @Column(name = "ALLOW_DINGTALK_INTEGRATION")
  private Boolean allowDingTalkIntegration;

  /* 登录会话的过期时间（小时） */
  @Column(name = "LOGIN_SESSION_EXPIRATION_HOURS")
  private Integer loginSessionExpirationHours;

  /* 登录掩码设置，指定允许登录的 IP 地址 */
  @ElementCollection
  @CollectionTable(
      name = "LOGIN_MASK_IPS",
      joinColumns = @JoinColumn(name = "USER_SECURITY_SETTINGS_ID"))
  @Column(name = "IP_ADDRESS")
  private List<String> allowedLoginIPs;
}
