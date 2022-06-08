package cn.asany.autoconfigure;

import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("cn.asany.website.*.domain")
@ComponentScan({
  "cn.asany.website.*.dao",
  "cn.asany.website.*.service",
  "cn.asany.website.*.graphql",
  "cn.asany.website.*.convert"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.website.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class WebsiteAutoConfiguration {}
