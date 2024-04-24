package cn.asany.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * System 自动配置
 *
 * @author limaofeng
 */
@Configuration
@Slf4j
@EntityScan({"cn.asany.system.domain"})
@ComponentScan({
  "cn.asany.system.dao",
  "cn.asany.system.convert",
  "cn.asany.system.service",
  "cn.asany.system.graphql",
  "cn.asany.system.web",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.system.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class SystemAutoConfiguration {}
