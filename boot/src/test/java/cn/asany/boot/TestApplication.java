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
package cn.asany.boot;

import graphql.kickstart.autoconfigure.tools.GraphQLJavaToolsAutoConfiguration;
import graphql.kickstart.autoconfigure.web.servlet.GraphQLWebAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.autoconfigure.GraphQLAutoConfiguration;
import net.asany.jfantasy.autoconfigure.SecurityAutoConfiguration;
import net.asany.jfantasy.graphql.autoconfigure.GraphQLSecurityAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.audit.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author limaofeng
 * @version V1.0 @Description: 测试程序入口
 * @date 2022/7/28 9:12 9:12
 */
@Slf4j
@Configuration
@EnableCaching
@EnableAutoConfiguration(
    exclude = {
      MongoAutoConfiguration.class,
      QuartzAutoConfiguration.class,
      WebMvcAutoConfiguration.class,
      AuditAutoConfiguration.class,
      GraphQLAutoConfiguration.class,
      GraphQLWebAutoConfiguration.class,
      GraphQLJavaToolsAutoConfiguration.class,
      SecurityAutoConfiguration.class,
      GraphQLSecurityAutoConfiguration.class
    })
public class TestApplication {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
