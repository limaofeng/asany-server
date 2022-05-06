package cn.asany.autoconfigure;

import cn.asany.weixin.framework.factory.WeixinSessionFactoryBean;
import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.listener.SubscribeListener;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("cn.asany.weixin.bean")
@ComponentScan({
  "cn.asany.weixin.dao",
  "cn.asany.weixin.service",
  "cn.asany.weixin.graphql",
  "cn.asany.weixin.convert"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.weixin.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class WeixinAutoConfiguration {

  private final ApplicationContext applicationContext;

  @Autowired
  public WeixinAutoConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Bean
  public SubscribeListener subscribeListener() {
    return new SubscribeListener();
  }

  @Bean
  public WeixinSessionFactoryBean weiXinSessionFactoryBean() {
    WeixinSessionFactoryBean weixinSessionFactoryBean = new WeixinSessionFactoryBean();
    weixinSessionFactoryBean.setApplicationContext(applicationContext);

    // 关注时,记录粉丝信息
    weixinSessionFactoryBean.addEventListener(
        EventMessage.EventType.subscribe, subscribeListener());

    return weixinSessionFactoryBean;
  }
}
