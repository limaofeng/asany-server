package cn.asany.autoconfigure;

import cn.asany.email.utils.ConfigLoader;
import java.io.IOException;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.james.filesystem.api.FileSystem;
import org.apache.james.pop3server.netty.OioPOP3ServerFactory;
import org.apache.james.pop3server.netty.POP3ServerFactory;
import org.apache.james.protocols.lib.handler.ProtocolHandlerLoader;
import org.jboss.netty.util.HashedWheelTimer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * POP3 协议服务自动配置
 *
 * @author limaofeng
 */
@Configuration
public class POP3ServerAutoConfiguration {

  private final ResourceLoader resourceLoader;

  public POP3ServerAutoConfiguration(final ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Bean
  public XMLConfiguration pop3ServerConfiguration() throws ConfigurationException {
    try {
      return ConfigLoader.getConfig(
          resourceLoader.getResource("classpath:pop3server.xml").getInputStream());
    } catch (IOException e) {
      throw new ConfigurationException(e.getMessage());
    }
  }

  @Bean(initMethod = "init", destroyMethod = "destroy")
  public POP3ServerFactory pop3ServerFactory(
      ProtocolHandlerLoader loader, FileSystem filesystem, HashedWheelTimer hashedWheelTimer)
      throws ConfigurationException {
    OioPOP3ServerFactory pop3ServerFactory = new OioPOP3ServerFactory();
    HierarchicalConfiguration<ImmutableNode> configuration = pop3ServerConfiguration();
    pop3ServerFactory.setProtocolHandlerLoader(loader);
    pop3ServerFactory.setFileSystem(filesystem);
    pop3ServerFactory.setHashedWheelTimer(hashedWheelTimer);
    pop3ServerFactory.configure(configuration);
    return pop3ServerFactory;
  }
}
