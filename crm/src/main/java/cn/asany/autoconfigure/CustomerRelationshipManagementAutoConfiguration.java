package cn.asany.autoconfigure;

import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({"cn.asany.crm.*.domain"})
@ComponentScan({
  "cn.asany.crm.*.dao",
  "cn.asany.crm.*.graphql",
  "cn.asany.crm.*.service",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.crm.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class CustomerRelationshipManagementAutoConfiguration {}
