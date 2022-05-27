package cn.asany.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.dom.MessageServiceFactory;
import org.apache.james.mime4j.stream.MimeConfig;
import org.apache.james.queue.api.MailQueueFactory;
import org.apache.james.queue.api.MailQueueItemDecoratorFactory;
import org.apache.james.queue.api.ManageableMailQueue;
import org.apache.james.queue.api.RawMailQueueItemDecoratorFactory;
import org.apache.james.queue.memory.MemoryMailQueueFactory;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Email 配置
 *
 * @author limaofeng
 */
@Configuration
@Slf4j
@EntityScan({"cn.asany.email*.domain"})
@ComponentScan({
  "cn.asany.email.*.dao",
  "cn.asany.email.*.convert",
  "cn.asany.email.*.service",
  "cn.asany.email.*.component",
  "cn.asany.email.*.graphql",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.email.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class EmailAutoConfiguration {

  @Bean
  public MailQueueItemDecoratorFactory mailQueueItemDecoratorFactory() {
    return new RawMailQueueItemDecoratorFactory();
  }

  @Bean
  public MailQueueFactory<ManageableMailQueue> mailQueueFactory() {
    return new MemoryMailQueueFactory(mailQueueItemDecoratorFactory());
  }

  @Bean
  public MessageServiceFactory newMessageBuilder() throws MimeException {
    MimeConfig mec = MimeConfig.PERMISSIVE;

    MessageServiceFactory mbf = MessageServiceFactory.newInstance();
    mbf.setAttribute("MimeEntityConfig", mec);
    mbf.setAttribute("FlatMode", true);
    mbf.setAttribute("ContentDecoding", false);

    return mbf;
  }

  @Bean
  public MessageBuilder defaultMailMessageBuilder(MessageServiceFactory factory) {
    return factory.newMessageBuilder();
  }
}
