package cn.asany.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 消息自动配置
 *
 * @author limaofeng
 */
@Slf4j
@Configuration
@EntityScan({
  "cn.asany.message.data.domain",
  "cn.asany.message.define.domain",
})
@ComponentScan({
  "cn.asany.message.core",
  "cn.asany.message.*.dao",
  "cn.asany.message.*.listener",
  "cn.asany.message.*.service",
  "cn.asany.message.*.graphql",
  "cn.asany.message.data.util",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.message.*.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class MessageAutoConfiguration {}
