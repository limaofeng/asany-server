package cn.asany.autoconfigure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Sunrise AutoConfiguration
 *
 * @author limaofeng
 */
@Configuration
@EntityScan("cn.asany.sunrise.*.bean")
@ComponentScan({
  "cn.asany.sunrise.*.dao",
  "cn.asany.sunrise.*.service",
  "cn.asany.sunrise.*.graphql",
  "cn.asany.sunrise.*.convert"
})
public class SunriseAutoConfiguration {}
