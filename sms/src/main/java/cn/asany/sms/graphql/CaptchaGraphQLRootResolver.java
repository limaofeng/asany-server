package cn.asany.sms.graphql;

import cn.asany.base.sms.CaptchaService;
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

  public CaptchaGraphQLRootResolver(CaptchaService captchaService) {
    this.captchaService = captchaService;
  }

  /** 发送验证码 */
  public String sendCaptcha(String configId, String sessionId, String phone, boolean force)
      throws ParseException {
    return this.captchaService.getChallengeForID(configId, sessionId, phone, force);
  }

  public Boolean validCaptcha(String configId, String sessionId, String code) {
    return this.captchaService.validateResponseForID(configId, sessionId, code);
  }
}
