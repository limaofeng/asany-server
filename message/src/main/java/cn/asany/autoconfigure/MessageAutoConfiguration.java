package cn.asany.autoconfigure;

import cn.asany.message.api.MessageSenderResolver;
import cn.asany.message.core.DefaultMessageSenderResolver;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 消息自动配置
 *
 * @author limaofeng
 */
@Configuration
@EntityScan({
  "cn.asany.message.data.domain",
  "cn.asany.message.define.domain",
})
@ComponentScan({
  "cn.asany.message.core",
  "cn.asany.message.*.dao",
  "cn.asany.message.*.converter",
  "cn.asany.message.*.service",
  "cn.asany.message.*.graphql",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.message.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class MessageAutoConfiguration {

}
