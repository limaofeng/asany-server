package cn.asany.autoconfigure;

import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
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
@EntityScan("cn.asany.sunrise.*.bean")
@ComponentScan({
  "cn.asany.sunrise.*.dao",
  "cn.asany.sunrise.*.service",
  "cn.asany.sunrise.*.graphql",
  "cn.asany.sunrise.*.convert"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.sunrise.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class SunriseAutoConfiguration {}
