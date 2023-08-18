package cn.asany.security.core.domain.enums;

import lombok.Getter;

/**
 * 多因素认证
 *
 * @author limaofeng
 */
@Getter
public enum MultiFactorAuthentication {
  MFA_DEVICE("MFA 设备"),
  CELLPHONE("手机"),
  EMAIL("邮箱");

  private final String displayName;

  MultiFactorAuthentication(String displayName) {
    this.displayName = displayName;
  }
}
