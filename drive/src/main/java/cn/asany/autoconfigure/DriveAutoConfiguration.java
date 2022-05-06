package cn.asany.autoconfigure;

import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({"cn.asany.drive.bean"})
@ComponentScan({
  "cn.asany.drive.dao",
  "cn.asany.drive.graphql",
  "cn.asany.drive.service",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.drive.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class DriveAutoConfiguration {}
