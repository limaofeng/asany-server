package cn.asany.autoconfigure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("cn.asany.website.*.bean")
@ComponentScan({
  "cn.asany.website.*.dao",
  "cn.asany.website.*.service",
  "cn.asany.website.*.graphql",
  "cn.asany.website.*.convert"
})
public class WebsiteAutoConfiguration {}
