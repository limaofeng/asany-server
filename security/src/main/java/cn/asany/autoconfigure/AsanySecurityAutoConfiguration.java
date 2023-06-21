package cn.asany.autoconfigure;

import cn.asany.security.core.domain.User;
import cn.asany.security.core.service.UserService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.dataloader.DataLoaderFactory;
import org.jfantasy.autoconfigure.OAuth2SecurityAutoConfiguration;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.crypto.password.DESPasswordEncoder;
import org.jfantasy.framework.security.crypto.password.MD5PasswordEncoder;
import org.jfantasy.framework.security.crypto.password.PasswordEncoder;
import org.jfantasy.framework.security.crypto.password.PlaintextPasswordEncoder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.context.DataLoaderRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author limaofeng
 */
@Configuration
@EntityScan({"cn.asany.security.*.domain"})
@ComponentScan({
  "cn.asany.security.*.dao",
  "cn.asany.security.*.convert",
  "cn.asany.security.*.service",
  "cn.asany.security.*.listener",
  "cn.asany.security.*.validators",
  "cn.asany.security.*.graphql",
  // TODO: 待确认是否需要
  //  "cn.asany.security.runner",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.security.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
@AutoConfigureBefore({OAuth2SecurityAutoConfiguration.class})
public class AsanySecurityAutoConfiguration {

  @Bean("asany.PasswordEncoder")
  public PasswordEncoder passwordEncoder(Environment environment) {
    String encoder = environment.getProperty("PASSWORD_ENCODER", "plaintext");
    switch (encoder) {
      case "des":
        return new DESPasswordEncoder();
      case "plaintext":
        return new PlaintextPasswordEncoder();
      case "md5":
      default:
        return new MD5PasswordEncoder();
    }
  }

  @Bean("user.DataLoaderRegistryCustomizer")
  public DataLoaderRegistryCustomizer dataLoaderRegistryCustomizer(UserService userService) {
    return registry ->
        registry.register(
            "userDataLoader",
            DataLoaderFactory.newDataLoader(
                ids ->
                    CompletableFuture.supplyAsync(
                        () -> {
                          List<User> users =
                              userService.findAll(PropertyFilter.newFilter().in("id", ids));
                          ids.replaceAll(value -> ObjectUtil.find(users, "id", value));
                          return ids;
                        })));
  }

  //    @Bean(name = "dingtalk.AuthenticationProvider")
  //    public SimpleAuthenticationProvider
  // dingtalkAuthenticationProvider(DingtalkUserDetailsService userDetailsService) {
  //        SimpleAuthenticationProvider provider = new
  // SimpleAuthenticationProvider(DingtalkAuthenticationToken.class);
  //        provider.setUserDetailsService(userDetailsService);
  //        return provider;
  //    }

  //    @Bean(name = "WeChat.AuthenticationProvider")
  //    public SimpleAuthenticationProvider weChatAuthenticationProvider(WeChatUserDetailsService
  // userDetailsService) {
  //        SimpleAuthenticationProvider provider = new
  // SimpleAuthenticationProvider(WeChatAuthenticationToken.class);
  //        provider.setUserDetailsService(userDetailsService);
  //        return provider;
  //    }

  //    @Bean(name = "anonymous.AuthenticationProvider")
  //    public SimpleAuthenticationProvider
  // anonymousAuthenticationProvider(AnonymousUserDetailsService userDetailsService) {
  //        SimpleAuthenticationProvider provider = new
  // SimpleAuthenticationProvider(AnonymousAuthenticationToken.class);
  //        provider.setUserDetailsService(userDetailsService);
  //        return provider;
  //    }

  //    @Bean(name = "temporary.AuthenticationProvider")
  //    public SimpleAuthenticationProvider
  // temporaryAuthenticationProvider(TemporaryUserDetailsService userDetailsService) {
  //        SimpleAuthenticationProvider provider = new
  // SimpleAuthenticationProvider(TemporaryAuthenticationToken.class);
  //        provider.setUserDetailsService(userDetailsService);
  //        return provider;
  //    }

}
