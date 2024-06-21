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

import cn.asany.autoconfigure.properties.SmsProviderProperties;
import cn.asany.sms.provider.ShortMessageServiceProviderFactory;
import cn.asany.sms.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 短信服务自动装配
 *
 * @author limaofeng
 */
@Configuration
@Slf4j
@EntityScan({"cn.asany.sms.domain"})
@ComponentScan({
  "cn.asany.sms.graphql",
  "cn.asany.sms.dao",
  "cn.asany.sms.service",
  "cn.asany.sms.graphql",
  "cn.asany.sms.listener"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.sms.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
@EnableConfigurationProperties({SmsProviderProperties.class})
public class SmsAutoConfiguration {

  @Bean
  public ShortMessageServiceProviderFactory shortMessageServiceProviderFactory(
      MessageService messageService) {
    return new ShortMessageServiceProviderFactory(messageService);
  }
}
