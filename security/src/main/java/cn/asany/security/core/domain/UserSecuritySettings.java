package cn.asany.security.core.domain;

import cn.asany.security.core.domain.enums.MultiFactorAuthentication;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户安全设置对象，用于存储和管理用户的安全设置。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSecuritySettings {
  private boolean saveMFAStatusFor7Days; // 保存 MFA 验证状态 7 天
  private boolean allowSelfManagedPassword; // 是否允许自主管理密码
  private boolean allowSelfManagedAccessKey; // 是否允许自主管理 AccessKey
  private boolean allowSelfManagedMFADevice; // 是否允许自主管理 MFA 设备
  private MFAMode loginMFAMode; // 登录时必须使用 MFA 的模式
  private MultiFactorAuthentication multiFactorAuthentication; // 多因素认证设置
  private boolean allowDingTalkIntegration; // 是否允许自主管理钉钉
  private boolean allowLongLoginInAliCloudApp; // 是否允许在阿里云 App 保持长登录
  private int loginSessionExpirationHours; // 登录会话的过期时间（小时）
  private List<String> loginMaskIPs; // 登录掩码设置，指定允许登录的 IP 地址
}
