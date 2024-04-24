package cn.asany.autoconfigure;

import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Sunrise AutoConfiguration
 *
 * @author limaofeng
 */
@Configuration
@EntityScan("cn.asany.sunrise.*.domain")
@ComponentScan({
  "cn.asany.sunrise.*.dao",
  "cn.asany.sunrise.*.service",
  "cn.asany.sunrise.*.graphql",
  "cn.asany.sunrise.*.convert"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.sunrise.*.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class SunriseAutoConfiguration {}
