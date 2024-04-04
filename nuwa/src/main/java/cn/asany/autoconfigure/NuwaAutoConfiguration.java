package cn.asany.autoconfigure;

import cn.asany.base.IApplicationModule;
import cn.asany.base.IModuleProperties;
import cn.asany.base.ModuleResolver;
import cn.asany.nuwa.module.DefaultModuleResolver;
import java.util.List;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
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
    repositoryBaseClass = ComplexJpaRepository.class)
public class NuwaAutoConfiguration {

  @Bean
  public ModuleResolver moduleResolver(
      List<IApplicationModule<? extends IModuleProperties>> modules) {
    return new DefaultModuleResolver(modules);
  }
}
