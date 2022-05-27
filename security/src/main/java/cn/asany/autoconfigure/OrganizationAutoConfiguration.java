package cn.asany.autoconfigure;

import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 自动配置
 *
 * @author limaofeng
 */
@Configuration
@EntityScan({
  "cn.asany.organization.core.domain",
  "cn.asany.organization.employee.domain",
  "cn.asany.organization.relationship.domain"
})
@ComponentScan({
  "cn.asany.organization.core.dao",
  "cn.asany.organization.core.convert",
  "cn.asany.organization.core.service",
  "cn.asany.organization.core.graphql",
  "cn.asany.organization.employee.dao",
  "cn.asany.organization.employee.converter",
  "cn.asany.organization.employee.service",
  "cn.asany.organization.employee.graphql",
  "cn.asany.organization.relationship.dao",
  "cn.asany.organization.relationship.converter",
  "cn.asany.organization.relationship.service",
  "cn.asany.organization.relationship.graphql",
})
@EnableJpaRepositories(
    basePackages = {
      "cn.asany.organization.core.dao",
      "cn.asany.organization.employee.dao",
      "cn.asany.organization.relationship.dao"
    },
    repositoryBaseClass = ComplexJpaRepository.class)
public class OrganizationAutoConfiguration {}
