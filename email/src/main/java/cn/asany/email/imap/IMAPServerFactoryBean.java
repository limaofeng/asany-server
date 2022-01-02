package cn.asany.email.imap;

import cn.asany.email.imap.manager.DefaultEventBus;
import org.apache.commons.configuration2.JSONConfiguration;
import org.apache.james.imap.encode.main.DefaultImapEncoderFactory;
import org.apache.james.imap.main.DefaultImapDecoderFactory;
import org.apache.james.imap.processor.main.DefaultImapProcessorFactory;
import org.apache.james.imapserver.netty.IMAPServer;
import org.apache.james.imapserver.netty.ImapMetrics;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.events.EventBus;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.exception.SubscriptionException;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.StoreMailboxManager;
import org.apache.james.mailbox.store.mail.*;
import org.apache.james.mailbox.store.quota.QuotaComponents;
import org.apache.james.mailbox.store.user.SubscriptionMapper;
import org.apache.james.metrics.api.MetricFactory;
import org.apache.james.metrics.api.NoopMetricFactory;
import org.apache.james.server.core.configuration.Configuration;
import org.apache.james.server.core.filesystem.FileSystemImpl;
import org.springframework.beans.factory.FactoryBean;

public class IMAPServerFactoryBean implements FactoryBean<IMAPServer> {

  private IMAPServer createImapServer() throws Exception {
    MetricFactory factory = new NoopMetricFactory();
    //        FakeAuthenticator authenticator = new FakeAuthenticator();
    //        authenticator.addUser(USER, USER_PASS);
    //        authenticator.addUser(USER2, USER_PASS);
    //
    //            InMemoryIntegrationResources memoryIntegrationResources =
    // InMemoryIntegrationResources.builder()
    ////            .authenticator(authenticator)
    ////            .authorizator(FakeAuthorizator.defaultReject())
    //            .inVmEventBus()
    //            .defaultAnnotationLimits()
    //            .defaultMessageParser()
    //            .scanningSearchIndex()
    //            .noPreDeletionHooks()
    //            .storeQuotaManager()
    //            .build();
    //        RecordingMetricFactory metricFactory = new RecordingMetricFactory();

    EventBus eventBus = new DefaultEventBus();

    MailboxManager mailboxManager =
        new StoreMailboxManager(
            new MailboxSessionMapperFactory() {
              @Override
              public AnnotationMapper createAnnotationMapper(MailboxSession session)
                  throws MailboxException {
                return null;
              }

              @Override
              public MessageMapper createMessageMapper(MailboxSession session)
                  throws MailboxException {
                return null;
              }

              @Override
              public MessageIdMapper createMessageIdMapper(MailboxSession session)
                  throws MailboxException {
                return null;
              }

              @Override
              public MailboxMapper createMailboxMapper(MailboxSession session)
                  throws MailboxException {
                return null;
              }

              @Override
              public SubscriptionMapper createSubscriptionMapper(MailboxSession session)
                  throws SubscriptionException {
                return null;
              }

              @Override
              public UidProvider getUidProvider() {
                return null;
              }

              @Override
              public ModSeqProvider getModSeqProvider() {
                return null;
              }
            },
            null,
            null,
            null,
            null,
            null,
            eventBus,
            null,
            new QuotaComponents(null, null, null, null),
            null,
            null,
            null);

    IMAPServer imapServer =
        new IMAPServer(
            DefaultImapDecoderFactory.createDecoder(),
            new DefaultImapEncoderFactory().buildImapEncoder(),
            DefaultImapProcessorFactory.createDefaultProcessor(
                mailboxManager, eventBus, null, null, null, null),
            new ImapMetrics(factory));

    Configuration configuration =
        Configuration.builder().workingDirectory("../").configurationFromClasspath().build();
    FileSystemImpl fileSystem = new FileSystemImpl(configuration.directories());
    imapServer.setFileSystem(fileSystem);

    JSONConfiguration config = new JSONConfiguration();

    config.addProperty("aa", "1213");
    config.addProperty("bind", "0.0.0.0:993");

    imapServer.configure(config);
    imapServer.init();

    return imapServer;
  }

  @Override
  public IMAPServer getObject() throws Exception {
    return createImapServer();
  }

  @Override
  public Class<?> getObjectType() {
    return IMAPServer.class;
  }
}
