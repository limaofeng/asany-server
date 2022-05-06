package cn.asany.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Slf4j
@EntityScan({"cn.asany.base.*.bean"})
@ComponentScan({
  "cn.asany.base.*.graphql",
  "cn.asany.base.*.dao",
  "cn.asany.base.*.service",
  "cn.asany.base.*.graphql",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.base.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class BaseAutoConfiguration {}
