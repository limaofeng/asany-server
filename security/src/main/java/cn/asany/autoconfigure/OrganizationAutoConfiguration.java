package cn.asany.autoconfigure;

import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
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
  "cn.asany.organization.employee.service",
  "cn.asany.organization.relationship.dao",
  "cn.asany.organization.relationship.service",
})
@EnableJpaRepositories(
    basePackages = {
      "cn.asany.organization.core.dao",
      "cn.asany.organization.employee.dao",
      "cn.asany.organization.relationship.dao"
    },
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class OrganizationAutoConfiguration {}
