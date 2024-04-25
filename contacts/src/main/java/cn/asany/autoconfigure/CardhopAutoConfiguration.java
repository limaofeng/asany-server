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

import cn.asany.cardhop.contacts.service.DefaultContactsServiceFactory;
import cn.asany.cardhop.integration.IContactsService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Cardhop 自动配置
 *
 * @author limaofeng
 */
@Configuration
@Slf4j
@EntityScan({"cn.asany.cardhop.contacts.domain"})
@ComponentScan({
  "cn.asany.cardhop.contacts.dao",
  "cn.asany.cardhop.contacts.service",
  "cn.asany.cardhop.contacts.graphql",
  "cn.asany.cardhop.integration.convert",
  "cn.asany.cardhop.integration.service",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.cardhop.contacts.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class CardhopAutoConfiguration {

  @Bean
  public DefaultContactsServiceFactory contactsServiceFactory(List<IContactsService> services) {
    return new DefaultContactsServiceFactory(services);
  }
}
