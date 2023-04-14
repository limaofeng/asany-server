package cn.asany.autoconfigure;

import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({
  "cn.asany.message.data.domain",
  "cn.asany.message.define.domain",
})
@ComponentScan({
  "cn.asany.message.*.dao",
  "cn.asany.message.*.converter",
  "cn.asany.message.*.service",
  "cn.asany.message.*.graphql",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.message.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class MessageAutoConfiguration {}
