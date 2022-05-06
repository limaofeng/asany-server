package cn.asany.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * System 配置
 *
 * @author limaofeng
 */
@Configuration
@Slf4j
@EntityScan({"cn.asany.system.bean"})
@ComponentScan({
  "cn.asany.system.dao",
  "cn.asany.system.convert",
  "cn.asany.system.service",
  "cn.asany.system.graphql",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.system.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class SystemAutoConfiguration {}
