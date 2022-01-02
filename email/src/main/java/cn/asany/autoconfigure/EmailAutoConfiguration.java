package cn.asany.autoconfigure;

import cn.asany.email.imap.IMAPServerFactoryBean;
import cn.asany.email.server.LoggingRcptHook;
import cn.asany.email.server.SmtpServerProperties;
import cn.asany.email.server.hooks.MyAuthHook2;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.protocols.api.handler.ProtocolHandler;
import org.apache.james.protocols.smtp.MailEnvelope;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.apache.james.protocols.smtp.hook.MessageHook;
import org.apache.james.queue.api.MailQueueFactory;
import org.apache.james.queue.api.MailQueueItemDecoratorFactory;
import org.apache.james.queue.api.ManageableMailQueue;
import org.apache.james.queue.api.RawMailQueueItemDecoratorFactory;
import org.apache.james.queue.memory.MemoryMailQueueFactory;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Email 配置
 *
 * @author limaofeng
 */
@Configuration
@Slf4j
@EntityScan({"cn.asany.email.*.bean"})
@ComponentScan({
  "cn.asany.email.*.dao",
  "cn.asany.email.*.component",
  "cn.asany.email.*.service",
  "cn.asany.email.data.rest",
})
@EnableConfigurationProperties(SmtpServerProperties.class)
public class EmailAutoConfiguration {

  @Bean
  public IMAPServerFactoryBean imapServerFactoryBean() {
    return new IMAPServerFactoryBean();
  }

  //  @Bean
  //  public SMTPServerAutoConfiguration smtpServerFactoryBean(Collection<ProtocolHandler> handlers)
  //      throws ConfigurationException {
  //    return new SMTPServerAutoConfiguration(handlers, protocolHandlerLoader(), dnsService());
  //  }

  public MailQueueItemDecoratorFactory mailQueueItemDecoratorFactory() {
    return new RawMailQueueItemDecoratorFactory();
  }

  @Bean
  public MailQueueFactory<ManageableMailQueue> mailQueueFactory() {
    return new MemoryMailQueueFactory(mailQueueItemDecoratorFactory());
  }

  //
  //  @Bean(initMethod = "start", destroyMethod = "stop")
  //  public SmtpServer smtpServer(
  //      SmtpServerProperties properties, Collection<ProtocolHandler> handlers) {
  //    return new SmtpServer(properties, handlers);
  //  }

  @Bean
  public ProtocolHandler loggingRcptHook() {
    return new LoggingRcptHook();
  }

  @Bean
  public ProtocolHandler customAuthHook() {
    return new MyAuthHook2();
  }

  @Bean
  public ProtocolHandler loggingMessageHook() {
    return new MessageHook() {
      @Override
      public HookResult onMessage(SMTPSession smtpSession, MailEnvelope mailEnvelope) {
        log.info(
            "mail from={} to={} size={}",
            mailEnvelope.getSender(),
            mailEnvelope.getRecipients(),
            mailEnvelope.getSize());

        try {
          String body = FileUtil.readFile(mailEnvelope.getMessageInputStream());
          System.out.println("body:" + body);
        } catch (IOException e) {
          e.printStackTrace();
        }

        return HookResult.OK;
      }

      @Override
      public void init(org.apache.commons.configuration2.Configuration config) {
        System.out.println(config);
      }

      @Override
      public void destroy() {
        System.out.println("destroy");
      }
    };
  }
}
