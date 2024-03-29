package cn.asany.autoconfigure;

import cn.asany.security.auth.graphql.directive.AuthDirective;
import cn.asany.security.auth.graphql.directive.ResourceDirective;
import cn.asany.security.auth.service.AuthInfoService;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.service.DefaultUserDetailsService;
import cn.asany.security.core.service.ResourceTypeService;
import cn.asany.security.core.service.UserService;
import cn.asany.security.oauth.job.TokenCleanupJob;
import graphql.kickstart.autoconfigure.tools.SchemaDirective;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.jfantasy.autoconfigure.OAuth2SecurityAutoConfiguration;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.core.userdetails.UserDetailsService;
import org.jfantasy.framework.security.crypto.password.DESPasswordEncoder;
import org.jfantasy.framework.security.crypto.password.MD5PasswordEncoder;
import org.jfantasy.framework.security.crypto.password.PasswordEncoder;
import org.jfantasy.framework.security.crypto.password.PlaintextPasswordEncoder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.context.DataLoaderRegistryCustomizer;
import org.jfantasy.schedule.service.TaskScheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

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
})
@EnableJpaRepositories(
    basePackages = "cn.asany.security.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
@AutoConfigureBefore({OAuth2SecurityAutoConfiguration.class})
public class AsanySecurityAutoConfiguration implements InitializingBean {

  private final TaskScheduler taskScheduler;
  private final TransactionTemplate transactionTemplate;

  public AsanySecurityAutoConfiguration(
      TaskScheduler taskScheduler, TransactionTemplate transactionTemplate) {
    this.taskScheduler = taskScheduler;
    this.transactionTemplate = transactionTemplate;
  }

  @Override
  public void afterPropertiesSet() {
    transactionTemplate.execute(
        (TransactionCallback<Void>)
            status -> {
              try {
                if (!taskScheduler.checkExists(TokenCleanupJob.JOBKEY_TOKEN_CLEANUP)) {
                  taskScheduler.addJob(TokenCleanupJob.JOBKEY_TOKEN_CLEANUP, TokenCleanupJob.class);
                }
              } catch (SchedulerException e) {
                status.setRollbackOnly();
              }
              return null;
            });
  }

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

  @Bean("user.DataLoader")
  public DataLoader<Long, User> userDataLoader(UserService userService) {
    return DataLoaderFactory.newDataLoader(
        ids ->
            CompletableFuture.supplyAsync(
                () -> {
                  List<User> users = userService.findAll(PropertyFilter.newFilter().in("id", ids));
                  return ids.stream()
                      .map(id -> ObjectUtil.find(users, "id", id))
                      .collect(Collectors.toList());
                }));
  }

  @Bean
  public UserDetailsService<?> userDetailsService(
      UserService userService,
      MessageSourceAccessor messages,
      @Qualifier("user.DataLoader") DataLoader<Long, User> userDataLoader) {
    return new DefaultUserDetailsService(userService, userDataLoader, messages);
  }

  @Bean("user.DataLoaderRegistryCustomizer")
  public DataLoaderRegistryCustomizer dataLoaderRegistryCustomizer(
      @Qualifier("user.DataLoader") DataLoader<?, ?> userDataLoader) {
    return registry -> registry.register("userDataLoader", userDataLoader);
  }

  @Bean
  public SchemaDirective authDirective(@Autowired AuthInfoService authInfoService) {
    return new SchemaDirective("authInfo", new AuthDirective(authInfoService));
  }

  @Bean
  public SchemaDirective resourceDirective(@Autowired ResourceTypeService resourceTypeService) {
    return new SchemaDirective("resource", new ResourceDirective(resourceTypeService));
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
