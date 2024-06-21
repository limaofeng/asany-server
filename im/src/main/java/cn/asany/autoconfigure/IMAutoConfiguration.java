/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
  public UserService userService(
      @Autowired StringRedisTemplate redisTemplate, @Autowired OpenIMProperties openIMProperties) {
    return new UserService(
        redisTemplate,
        openIMProperties.getUrl(),
        openIMProperties.getSecret(),
        openIMProperties.getAdmin());
  }
}
