package cn.asany.autoconfigure;

import cn.asany.base.IModule;
import cn.asany.base.IModuleLoader;
import cn.asany.nuwa.module.DefaultModuleLoader;
import java.util.List;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Nuwa Auto Configuration
 *
 * @author limaofeng
 */
@Configuration
@EntityScan("cn.asany.nuwa.*.bean")
@ComponentScan({
  "cn.asany.nuwa.*.dao",
  "cn.asany.nuwa.*.service",
  "cn.asany.nuwa.*.runner",
  "cn.asany.nuwa.*.converter",
  "cn.asany.nuwa.*.graphql",
})
public class NuwaAutoConfiguration {

  @Bean
  public IModuleLoader moduleLoader(List<IModule> modules) {
    return new DefaultModuleLoader(modules);
  }
}
