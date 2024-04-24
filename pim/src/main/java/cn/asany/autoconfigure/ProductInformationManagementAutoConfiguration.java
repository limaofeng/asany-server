package cn.asany.autoconfigure;

import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({"cn.asany.pim.*.domain"})
@ComponentScan({
  "cn.asany.pim.*.dao",
  "cn.asany.pim.*.graphql",
  "cn.asany.pim.*.component",
  "cn.asany.pim.*.convert",
  "cn.asany.pim.*.service",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.pim.*.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class ProductInformationManagementAutoConfiguration {}
