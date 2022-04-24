package cn.asany.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
public class SystemAutoConfiguration {}
