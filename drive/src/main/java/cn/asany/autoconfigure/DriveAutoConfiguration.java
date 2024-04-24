package cn.asany.autoconfigure;

import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({"cn.asany.drive.domain"})
@ComponentScan({
  "cn.asany.drive.dao",
  "cn.asany.drive.graphql",
  "cn.asany.drive.service",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.drive.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class DriveAutoConfiguration {}
