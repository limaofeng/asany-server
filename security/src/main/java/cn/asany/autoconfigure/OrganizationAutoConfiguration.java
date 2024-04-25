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

import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 自动配置
 *
 * @author limaofeng
 */
@Configuration
@EntityScan({
  "cn.asany.organization.core.domain",
  "cn.asany.organization.employee.domain",
  "cn.asany.organization.relationship.domain"
})
@ComponentScan({
  "cn.asany.organization.core.dao",
  "cn.asany.organization.core.convert",
  "cn.asany.organization.core.service",
  "cn.asany.organization.core.graphql",
  "cn.asany.organization.employee.dao",
  "cn.asany.organization.employee.service",
  "cn.asany.organization.relationship.dao",
  "cn.asany.organization.relationship.service",
})
@EnableJpaRepositories(
    basePackages = {
      "cn.asany.organization.core.dao",
      "cn.asany.organization.employee.dao",
      "cn.asany.organization.relationship.dao"
    },
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class OrganizationAutoConfiguration {}
