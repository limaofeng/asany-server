package cn.asany.myjob.autoconfigure;

import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({
  "cn.asany.myjob.factory.domain",
})
@ComponentScan({
  "cn.asany.myjob.factory.dao",
  "cn.asany.myjob.factory.service",
  "cn.asany.myjob.factory.convert",
  "cn.asany.myjob.factory.listener",
  "cn.asany.myjob.factory.graphql",
})
@EnableJpaRepositories(
    basePackages = {"cn.asany.myjob.factory.dao"},
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class FactoryScreenAutoConfiguration {}
