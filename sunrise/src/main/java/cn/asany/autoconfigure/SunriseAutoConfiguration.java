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
 * Sunrise AutoConfiguration
 *
 * @author limaofeng
 */
@Configuration
@EntityScan("cn.asany.sunrise.*.domain")
@ComponentScan({
  "cn.asany.sunrise.*.dao",
  "cn.asany.sunrise.*.service",
  "cn.asany.sunrise.*.graphql",
  "cn.asany.sunrise.*.convert"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.sunrise.*.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class SunriseAutoConfiguration {}
