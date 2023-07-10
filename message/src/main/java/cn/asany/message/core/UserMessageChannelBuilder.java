package cn.asany.message.core;

import cn.asany.message.api.MSChannelConfig;
import cn.asany.message.api.MessageChannelBuilder;
import cn.asany.message.data.service.UserMessageService;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * WebPush 消息发送者构建器
 *
 * @author limaofeng
 */
@Component
public class UserMessageChannelBuilder
    implements MessageChannelBuilder<UserMessageChannel, MSChannelConfig> {

  private final UserMessageService userMessageService;

  private final UserDetailsService<LoginUser> userDetailsService;

  public UserMessageChannelBuilder(
      UserMessageService userMessageService, UserDetailsService<LoginUser> userDetailsService) {
    this.userMessageService = userMessageService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public boolean supports(Class<MSChannelConfig> clazz) {
    return MSChannelConfig.class.isAssignableFrom(clazz);
  }

  @Override
  public UserMessageChannel build(MSChannelConfig config) {
    return new UserMessageChannel(this.userDetailsService, userMessageService, config);
  }
}
