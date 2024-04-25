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

import cn.asany.base.common.TicketTarget;
import cn.asany.base.common.TicketTargetBuilder;
import cn.asany.base.common.TicketTargetResolver;
import cn.asany.crm.support.component.DefaultTicketTargetResolver;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({"cn.asany.crm.*.domain"})
@ComponentScan({
  "cn.asany.crm.*.dao",
  "cn.asany.crm.*.graphql",
  "cn.asany.crm.*.service",
  "cn.asany.crm.*.convert"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.crm.*.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class CustomerRelationshipManagementAutoConfiguration {

  @Bean
  public TicketTargetResolver ticketTargetResolver(
      List<TicketTargetBuilder<? extends TicketTarget>> builders) {
    return new DefaultTicketTargetResolver(builders);
  }
}
