package cn.asany.autoconfigure;

import cn.asany.email.utils.ConfigLoader;
import java.io.IOException;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.james.filesystem.api.FileSystem;
import org.apache.james.imap.api.process.ImapProcessor;
import org.apache.james.imap.decode.ImapDecoder;
import org.apache.james.imap.decode.ImapDecoderFactory;
import org.apache.james.imap.encode.ImapEncoder;
import org.apache.james.imap.encode.ImapEncoderFactory;
import org.apache.james.imap.encode.main.DefaultImapEncoderFactory;
import org.apache.james.imap.main.DefaultImapDecoderFactory;
import org.apache.james.imap.processor.main.DefaultImapProcessorFactory;
import org.apache.james.imapserver.netty.IMAPServerFactory;
import org.apache.james.imapserver.netty.OioIMAPServerFactory;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.events.EventBus;
import org.apache.james.mailbox.quota.QuotaManager;
import org.apache.james.mailbox.quota.QuotaRootResolver;
import org.apache.james.mailbox.store.StoreSubscriptionManager;
import org.apache.james.metrics.api.MetricFactory;
import org.jboss.netty.util.HashedWheelTimer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * Imap Server AutoConfiguration
 *
 * @author limaofeng
 */
@Configuration
public class ImapServerAutoConfiguration {

  private final ResourceLoader resourceLoader;

  public ImapServerAutoConfiguration(final ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Bean
  public XMLConfiguration imapServerConfiguration() throws ConfigurationException {
    try {
      return ConfigLoader.getConfig(
          resourceLoader.getResource("classpath:imapserver.xml").getInputStream());
    } catch (IOException e) {
      throw new ConfigurationException(e.getMessage());
    }
  }

  @Bean(initMethod = "init", destroyMethod = "destroy")
  public IMAPServerFactory imapServerFactory(
      FileSystem fileSystem,
      ImapProcessor imapProcessor,
      MetricFactory metricFactory,
      HashedWheelTimer hashedWheelTimer)
      throws Exception {
    IMAPServerFactory imapServerFactory =
        new OioIMAPServerFactory(
            fileSystem,
            imapDecoder(),
            imapEncoder(),
            imapProcessor,
            metricFactory,
            hashedWheelTimer);
    XMLConfiguration configuration = imapServerConfiguration();
    imapServerFactory.configure(configuration);
    return imapServerFactory;
  }

  @Bean
  public ImapDecoderFactory imapDecoderFactory() {
    return new DefaultImapDecoderFactory();
  }

  @Bean
  public ImapDecoder imapDecoder() {
    return imapDecoderFactory().buildImapDecoder();
  }

  @Bean
  public ImapEncoderFactory imapEncoderFactory() {
    return new DefaultImapEncoderFactory();
  }

  @Bean
  public ImapEncoder imapEncoder() {
    return imapEncoderFactory().buildImapEncoder();
  }

  @Bean
  public ImapProcessor imapProcessor(
      MailboxManager mailboxManager,
      EventBus eventBus,
      StoreSubscriptionManager subscriptionManager,
      QuotaManager quotaManager,
      QuotaRootResolver quotaRootResolver,
      MetricFactory metricFactory) {
    return DefaultImapProcessorFactory.createXListSupportingProcessor(
        mailboxManager,
        eventBus,
        subscriptionManager,
        null,
        quotaManager,
        quotaRootResolver,
        metricFactory);
  }
}
