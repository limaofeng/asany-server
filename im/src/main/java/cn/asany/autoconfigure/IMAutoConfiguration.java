package cn.asany.autoconfigure;

import cn.asany.autoconfigure.properties.OpenIMProperties;
import cn.asany.im.auth.service.AuthService;
import cn.asany.im.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@Slf4j
@EntityScan({"cn.asany.im.*.domain"})
@ComponentScan({
  "cn.asany.im.*.service",
  "cn.asany.im.*.listener",
  "cn.asany.im.*.graphql",
})
@EnableConfigurationProperties({OpenIMProperties.class})
public class IMAutoConfiguration {

  @Bean("im.AuthService")
  public AuthService authService(
      @Autowired StringRedisTemplate redisTemplate, @Autowired OpenIMProperties openIMProperties) {
    return new AuthService(
        redisTemplate,
        openIMProperties.getUrl(),
        openIMProperties.getSecret(),
        openIMProperties.getAdmin());
  }

  @Bean("im.UserService")
  public UserService userService(@Autowired OpenIMProperties openIMProperties) {
    return new UserService(
        openIMProperties.getUrl(), openIMProperties.getSecret(), openIMProperties.getAdmin());
  }
}
