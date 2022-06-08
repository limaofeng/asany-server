package cn.asany.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@EntityScan({"cn.asany.base.*.domain"})
@ComponentScan({
  "cn.asany.base.*.service",
  "cn.asany.base.*.graphql",
})
public class BaseAutoConfiguration {}
