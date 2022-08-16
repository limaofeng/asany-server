package cn.asany.sms.graphql;

import cn.asany.sms.service.CaptchaService;
import cn.asany.sms.service.ValidationCaptchaService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.text.ParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 验证码
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class CaptchaGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final CaptchaService captchaService;
  private final ValidationCaptchaService validationCaptchaService;

  public CaptchaGraphQLRootResolver(
      CaptchaService captchaService, ValidationCaptchaService validationCaptchaService) {
    this.captchaService = captchaService;
    this.validationCaptchaService = validationCaptchaService;
  }

  /** 发送验证码 */
  public String sendCaptcha(String configId, String sessionId, String phone, boolean force)
      throws ParseException {
    return this.validationCaptchaService.getChallengeForID(configId, sessionId, phone, force);
  }

  public Boolean validCaptcha(String configId, String sessionId, String code) {
    return this.validationCaptchaService.validateResponseForID(configId, sessionId, code);
  }
}
