package cn.asany.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Slf4j
@EntityScan({"cn.asany.openapi.bean"})
@ComponentScan({
  "cn.asany.openapi.graphql",
  "cn.asany.openapi.dao",
  "cn.asany.openapi.service",
  "cn.asany.openapi.graphql",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.openapi.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class OpenapiAutoConfiguration {}
