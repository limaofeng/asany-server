package cn.asany.autoconfigure;

import cn.asany.autoconfigure.properties.FileSystemProperties;
import cn.asany.autoconfigure.properties.JamesProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.container.spring.bean.factory.protocols.ProtocolHandlerLoaderBeanFactory;
import org.apache.james.container.spring.filesystem.ResourceLoaderFileSystem;
import org.apache.james.filesystem.api.FileSystem;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.dom.MessageServiceFactory;
import org.apache.james.mime4j.stream.MimeConfig;
import org.apache.james.protocols.lib.handler.ProtocolHandlerLoader;
import org.apache.james.queue.api.MailQueueFactory;
import org.apache.james.queue.api.MailQueueItemDecoratorFactory;
import org.apache.james.queue.api.ManageableMailQueue;
import org.apache.james.queue.api.RawMailQueueItemDecoratorFactory;
import org.apache.james.queue.memory.MemoryMailQueueFactory;
import org.apache.james.server.core.filesystem.FileSystemImpl;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Email 配置
 *
 * @author limaofeng
 */
@Slf4j
@Configuration
@EntityScan({"cn.asany.email.*.domain"})
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
@EnableConfigurationProperties({JamesProperties.class, FileSystemProperties.class})
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

  @Bean
  public ProtocolHandlerLoader protocolHandlerLoader() {
    return new ProtocolHandlerLoaderBeanFactory();
  }

  @Bean
  public FileSystem fileSystem(JamesProperties jamesProperties) {
    FileSystemProperties properties = jamesProperties.getFilesystem();
    switch (properties.getType()) {
      case "spring":
        return new ResourceLoaderFileSystem();
      case "local":
        org.apache.james.server.core.configuration.Configuration configuration =
            org.apache.james.server.core.configuration.Configuration.builder()
                .workingDirectory(properties.getWorkingDirectory())
                .configurationFromClasspath()
                .build();
        return new FileSystemImpl(configuration.directories());
      default:
        throw new RuntimeException("不支持的文件系统类型");
    }
  }
}
