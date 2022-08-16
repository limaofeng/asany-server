package cn.asany.base.sms;

public interface CaptchaValidator {

  boolean validate(String config, String sessionId, String value);
}
