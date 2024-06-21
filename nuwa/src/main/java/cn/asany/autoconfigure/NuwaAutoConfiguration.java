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

import cn.asany.base.IApplicationModule;
import cn.asany.base.IModuleProperties;
import cn.asany.base.ModuleResolver;
import cn.asany.nuwa.module.DefaultModuleResolver;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Nuwa Auto Configuration
 *
 * @author limaofeng
 */
@Configuration
@EntityScan("cn.asany.nuwa.*.domain")
@ComponentScan({
  "cn.asany.nuwa.*.dao",
  "cn.asany.nuwa.*.service",
  "cn.asany.nuwa.*.converter",
  "cn.asany.nuwa.*.graphql",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.nuwa.*.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class NuwaAutoConfiguration {

  @Bean
  public ModuleResolver moduleResolver(
      List<IApplicationModule<? extends IModuleProperties>> modules) {
    return new DefaultModuleResolver(modules);
  }
}
