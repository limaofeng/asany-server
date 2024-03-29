package cn.asany.message.core;

import cn.asany.message.api.MSChannelConfig;
import cn.asany.message.api.Message;
import cn.asany.message.api.MessageChannel;
import cn.asany.message.api.SimpleMessage;
import cn.asany.message.data.domain.UserMessage;
import cn.asany.message.data.service.UserMessageService;
import cn.asany.security.core.domain.User;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.core.userdetails.UserDetailsService;
import org.jfantasy.framework.util.HandlebarsTemplateUtils;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.messaging.MessagingException;

/**
 * WebPush 消息发送者
 *
 * @author limaofeng
 */
@Slf4j
public class UserMessageChannel implements MessageChannel {

  private final UserMessageService userMessageService;
  private final MSChannelConfig config;

  private final UserDetailsService<LoginUser> userDetailsService;

  public UserMessageChannel(
      UserDetailsService<LoginUser> userDetailsService,
      UserMessageService userMessageService,
      MSChannelConfig config) {
    this.userMessageService = userMessageService;
    this.userDetailsService = userDetailsService;
    this.config = config;
    log.info("WebPushMessageSender init success, config: {}", config);
  }

  @Override
  public void send(Message message) throws MessagingException {
    SimpleMessage simpleMessage = (SimpleMessage) message;
    if (StringUtil.isBlank(simpleMessage.getSubject())) {
      throw new RuntimeException("subject is blank");
    }
    if (StringUtil.isBlank(simpleMessage.getText())) {
      throw new RuntimeException("text is blank");
    }
    for (String recipient : simpleMessage.getTo()) {
      LoginUser loginUser = userDetailsService.loadUserById(Long.valueOf(recipient)).join();

      log.info("send message to {}", loginUser.getUsername());

      Map<String, Object> data = new HashMap<>(simpleMessage.getTemplateParams());
      data.put("user", loginUser);

      String title =
          HandlebarsTemplateUtils.processTemplateIntoString(simpleMessage.getSubject(), data);
      String content =
          HandlebarsTemplateUtils.processTemplateIntoString(simpleMessage.getText(), data);

      userMessageService.save(
          UserMessage.builder()
              .read(false)
              .user(User.builder().id(Long.valueOf(recipient)).build())
              .title(title)
              .content(content)
              .type(simpleMessage.getOriginalMessageType())
              .message(simpleMessage.getOriginalMessage())
              .uri(simpleMessage.getUri())
              .build());
    }
  }
}
