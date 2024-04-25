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

import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 消息自动配置
 *
 * @author limaofeng
 */
@Slf4j
@Configuration
@EntityScan({
  "cn.asany.message.data.domain",
  "cn.asany.message.define.domain",
})
@ComponentScan({
  "cn.asany.message.core",
  "cn.asany.message.*.dao",
  "cn.asany.message.*.listener",
  "cn.asany.message.*.service",
  "cn.asany.message.*.graphql",
  "cn.asany.message.data.util",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.message.*.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class MessageAutoConfiguration {}
