package cn.asany.autoconfigure;

import cn.asany.email.mailbox.component.JPAMailboxManager;
import cn.asany.email.mailbox.component.JPAMailboxSessionMapperFactory;
import cn.asany.email.mailbox.component.JPASubscriptionManager;
import cn.asany.email.mailbox.component.mail.JPAModSeqProvider;
import cn.asany.email.mailbox.component.mail.JPAUidProvider;
import cn.asany.email.quota.component.JpaCurrentQuotaManager;
import cn.asany.email.rrt.component.JPARecipientRewriteTable;
import cn.asany.email.utils.ConfigLoader;
import com.codahale.metrics.MetricRegistry;
import java.io.IOException;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.james.adapter.mailbox.store.UserRepositoryAuthenticator;
import org.apache.james.adapter.mailbox.store.UserRepositoryAuthorizator;
import org.apache.james.container.spring.bean.factory.mailetcontainer.MailetLoaderBeanFactory;
import org.apache.james.container.spring.bean.factory.mailetcontainer.MatcherLoaderBeanFactory;
import org.apache.james.container.spring.bean.factory.mailrepositorystore.MailRepositoryStoreBeanFactory;
import org.apache.james.dnsservice.api.DNSService;
import org.apache.james.dnsservice.dnsjava.DNSJavaService;
import org.apache.james.domainlist.api.DomainList;
import org.apache.james.filesystem.api.FileSystem;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.MailboxPathLocker;
import org.apache.james.mailbox.acl.GroupMembershipResolver;
import org.apache.james.mailbox.acl.MailboxACLResolver;
import org.apache.james.mailbox.acl.SimpleGroupMembershipResolver;
import org.apache.james.mailbox.acl.UnionMailboxACLResolver;
import org.apache.james.mailbox.events.*;
import org.apache.james.mailbox.events.delivery.EventDelivery;
import org.apache.james.mailbox.events.delivery.InVmEventDelivery;
import org.apache.james.mailbox.extension.PreDeletionHook;
import org.apache.james.mailbox.model.MessageId;
import org.apache.james.mailbox.quota.MaxQuotaManager;
import org.apache.james.mailbox.quota.QuotaManager;
import org.apache.james.mailbox.quota.QuotaRootResolver;
import org.apache.james.mailbox.store.*;
import org.apache.james.mailbox.store.extractor.DefaultTextExtractor;
import org.apache.james.mailbox.store.mail.ModSeqProvider;
import org.apache.james.mailbox.store.mail.UidProvider;
import org.apache.james.mailbox.store.mail.model.DefaultMessageId;
import org.apache.james.mailbox.store.mail.model.impl.MessageParser;
import org.apache.james.mailbox.store.quota.*;
import org.apache.james.mailbox.store.search.SimpleMessageSearchIndex;
import org.apache.james.mailetcontainer.api.MailProcessor;
import org.apache.james.mailetcontainer.api.MatcherLoader;
import org.apache.james.mailetcontainer.impl.JamesMailSpooler;
import org.apache.james.mailetcontainer.impl.JamesMailetContext;
import org.apache.james.mailetcontainer.impl.camel.CamelCompositeProcessor;
import org.apache.james.metrics.api.MetricFactory;
import org.apache.james.metrics.dropwizard.DropWizardMetricFactory;
import org.apache.james.protocols.lib.handler.ProtocolHandlerLoader;
import org.apache.james.queue.api.MailQueueFactory;
import org.apache.james.rrt.api.RecipientRewriteTable;
import org.apache.james.sieverepository.api.SieveRepository;
import org.apache.james.sieverepository.file.SieveFileRepository;
import org.apache.james.smtpserver.netty.OioSMTPServerFactory;
import org.apache.james.smtpserver.netty.SMTPServerFactory;
import org.apache.james.user.api.UsersRepository;
import org.jboss.netty.util.HashedWheelTimer;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * SMTP 服务配置
 *
 * @author limaofeng
 */
@Configuration
public class SmtpServerAutoConfiguration {

  private final ResourceLoader resourceLoader;

  public SmtpServerAutoConfiguration(final ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Bean
  public XMLConfiguration mailetContainerConfiguration() throws ConfigurationException {
    try {
      return ConfigLoader.getConfig(
          resourceLoader.getResource("classpath:mailetcontainer.xml").getInputStream());
    } catch (IOException e) {
      throw new ConfigurationException(e.getMessage());
    }
  }

  @Bean
  public XMLConfiguration dnsServiceConfiguration() throws ConfigurationException {
    try {
      return ConfigLoader.getConfig(
          resourceLoader.getResource("classpath:dnsservice.xml").getInputStream());
    } catch (IOException e) {
      throw new ConfigurationException(e.getMessage());
    }
  }

  @Bean
  public XMLConfiguration mailRepositoryStoreConfiguration() throws ConfigurationException {
    try {
      return ConfigLoader.getConfig(
          resourceLoader.getResource("classpath:mailrepositorystore.xml").getInputStream());
    } catch (IOException e) {
      throw new ConfigurationException(e.getMessage());
    }
  }

  @Bean
  public XMLConfiguration smtpServerConfiguration() throws ConfigurationException {
    try {
      return ConfigLoader.getConfig(
          resourceLoader.getResource("classpath:smtpserver.xml").getInputStream());
    } catch (IOException e) {
      throw new ConfigurationException(e.getMessage());
    }
  }

  @Bean
  public RecipientRewriteTable recipientRewriteTable(DomainList domainList)
      throws ConfigurationException {
    JPARecipientRewriteTable rrt = new JPARecipientRewriteTable();
    rrt.configure(new BaseHierarchicalConfiguration());
    rrt.setDomainList(domainList);
    return rrt;
  }

  @Bean(initMethod = "init")
  public DNSJavaService dnsService() throws ConfigurationException {
    DNSJavaService dnsService = new DNSJavaService(metricFactory());
    XMLConfiguration configuration = dnsServiceConfiguration();
    dnsService.configure(configuration);
    return dnsService;
  }

  @Bean
  public MetricRegistry metricRegistry() {
    return new MetricRegistry();
  }

  @Bean
  public MetricFactory metricFactory() {
    return new DropWizardMetricFactory(metricRegistry());
  }

  @Bean
  public HashedWheelTimer hashedWheelTimer() {
    return new HashedWheelTimer();
  }

  @Bean(initMethod = "init", destroyMethod = "destroy")
  public SMTPServerFactory smtpServerFactory(ProtocolHandlerLoader loader, FileSystem fileSystem)
      throws Exception {
    OioSMTPServerFactory smtpServerFactory =
        new OioSMTPServerFactory(
            dnsService(), loader, fileSystem, metricFactory(), hashedWheelTimer());
    XMLConfiguration configuration = smtpServerConfiguration();
    smtpServerFactory.configure(configuration);
    return smtpServerFactory;
  }

  @Bean
  public MailRepositoryStoreBeanFactory mailRepositoryStore() throws ConfigurationException {
    MailRepositoryStoreBeanFactory beanFactory = new MailRepositoryStoreBeanFactory();
    XMLConfiguration configuration = mailRepositoryStoreConfiguration();
    beanFactory.configure(configuration);
    return beanFactory;
  }

  @Bean
  public SpringCamelContext camelContext() {
    return new SpringCamelContext();
  }

  @Bean
  public MailetLoaderBeanFactory mailetLoader() {
    return new MailetLoaderBeanFactory();
  }

  @Bean
  public MatcherLoader matcherLoader() {
    return new MatcherLoaderBeanFactory();
  }

  @Bean("jpa-uidProvider")
  public UidProvider uidProvider() {
    return new JPAUidProvider(jvmLocker());
  }

  @Bean("jpa-modSeqProvider")
  public ModSeqProvider modSeqProvider() {
    return new JPAModSeqProvider(jvmLocker());
  }

  @Bean
  public JPAMailboxSessionMapperFactory mailboxSessionMapperFactory() {
    EntityManagerFactory entityManagerFactory = SpringBeanUtils.getBean(EntityManagerFactory.class);
    return new JPAMailboxSessionMapperFactory(
        entityManagerFactory, uidProvider(), modSeqProvider());
  }

  @Bean
  public UserRepositoryAuthenticator authenticator(UsersRepository usersRepository) {
    return new UserRepositoryAuthenticator(usersRepository);
  }

  @Bean
  public UserRepositoryAuthorizator authorizator(UsersRepository usersRepository) {
    return new UserRepositoryAuthorizator(usersRepository);
  }

  @Bean
  public SessionProvider sessionProvider(UsersRepository usersRepository) {
    return new SessionProvider(authenticator(usersRepository), authorizator(usersRepository));
  }

  @Bean
  public MailboxPathLocker jvmLocker() {
    return new JVMMailboxPathLocker();
  }

  @Bean
  public MessageParser messageParser() {
    return new MessageParser();
  }

  @Bean
  public MessageId.Factory messageIdFactory() {
    return new DefaultMessageId.Factory();
  }

  @Bean
  public EventBus eventBus() {
    return new InVMEventBus(eventDelivery(), RetryBackoffConfiguration.DEFAULT, eventDeadletters());
  }

  @Bean
  public EventDelivery eventDelivery() {
    return new InVmEventDelivery(metricFactory());
  }

  @Bean
  public EventDeadLetters eventDeadletters() {
    return new MemoryEventDeadLetters();
  }

  @Bean
  public MailboxACLResolver aclResolver() {
    return new UnionMailboxACLResolver();
  }

  @Bean
  public GroupMembershipResolver groupMembershipResolver() {
    return new SimpleGroupMembershipResolver();
  }

  @Bean
  public StoreRightManager storeRightManager() {
    return new StoreRightManager(
        mailboxSessionMapperFactory(), aclResolver(), groupMembershipResolver(), eventBus());
  }

  @Bean
  public StoreMailboxAnnotationManager storeMailboxAnnotationManager() {
    return new StoreMailboxAnnotationManager(mailboxSessionMapperFactory(), storeRightManager());
  }

  @Bean
  public MaxQuotaManager maxQuotaManager() {
    return new FixedMaxQuotaManager();
  }

  @Bean
  public QuotaRootResolver quotaRootResolver(SessionProvider sessionProvider) {
    return new DefaultUserQuotaRootResolver(sessionProvider, mailboxSessionMapperFactory());
  }

  @Bean
  public CurrentQuotaCalculator currentQuotaCalculator(QuotaRootResolver quotaRootResolver) {
    return new CurrentQuotaCalculator(mailboxSessionMapperFactory(), quotaRootResolver);
  }

  @Bean
  public StoreCurrentQuotaManager storeCurrentQuotaManager() {
    return SpringBeanUtils.getBean(JpaCurrentQuotaManager.class);
  }

  @Bean
  public QuotaManager quotaManager() {
    return new StoreQuotaManager(storeCurrentQuotaManager(), maxQuotaManager());
  }

  @Bean
  public QuotaUpdater quotaUpdater(QuotaRootResolver quotaRootResolver) {
    return new ListeningCurrentQuotaUpdater(
        storeCurrentQuotaManager(), quotaRootResolver, eventBus(), quotaManager());
  }

  @Bean
  public QuotaComponents quotaComponents(QuotaRootResolver quotaRootResolver) {
    return new QuotaComponents(
        maxQuotaManager(), quotaManager(), quotaRootResolver, quotaUpdater(quotaRootResolver));
  }

  @Bean
  public PreDeletionHooks preDeletionHooks(Set<PreDeletionHook> hooks) {
    return new PreDeletionHooks(hooks, metricFactory());
  }

  @Bean("mailboxmanager")
  public MailboxManager mailboxManager(
      SessionProvider sessionProvider, QuotaComponents quotaComponents) {
    SimpleMessageSearchIndex searchIndex =
        new SimpleMessageSearchIndex(
            mailboxSessionMapperFactory(),
            mailboxSessionMapperFactory(),
            new DefaultTextExtractor());
    return new JPAMailboxManager(
        mailboxSessionMapperFactory(),
        sessionProvider,
        messageParser(),
        messageIdFactory(),
        eventBus(),
        storeMailboxAnnotationManager(),
        storeRightManager(),
        quotaComponents,
        searchIndex);
  }

  @Bean
  public StoreSubscriptionManager subscriptionManager() {
    return new JPASubscriptionManager(mailboxSessionMapperFactory());
  }

  @Bean
  public SieveRepository sieveRepository(FileSystem fileSystem) throws IOException {
    //noinspection VulnerableCodeUsages
    return new SieveFileRepository(fileSystem);
  }

  @Bean
  public MailProcessor mailProcessor() throws ConfigurationException {
    CamelCompositeProcessor mailetProcessor = new CamelCompositeProcessor(metricFactory());
    mailetProcessor.setMailetLoader(mailetLoader());
    mailetProcessor.setMatcherLoader(matcherLoader());
    mailetProcessor.configure(mailetContainerConfiguration().configurationAt("processors"));
    mailetProcessor.setCamelContext(camelContext());
    return mailetProcessor;
  }

  @Bean
  public JamesMailetContext mailetContext(
      DNSService dns,
      UsersRepository usersRepository,
      DomainList domains,
      MailQueueFactory<?> mailQueueFactory)
      throws ConfigurationException {
    JamesMailetContext context =
        new JamesMailetContext(dns, usersRepository, domains, mailQueueFactory);
    context.configure(mailetContainerConfiguration().configurationAt("context"));
    return context;
  }

  @Bean
  public JamesMailSpooler mailSpooler(MetricFactory metricFactory) throws ConfigurationException {
    JamesMailSpooler mailSpooler = new JamesMailSpooler(metricFactory);
    mailSpooler.configure(mailetContainerConfiguration().configurationAt("spooler"));
    return mailSpooler;
  }
}
