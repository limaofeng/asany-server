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

import cn.asany.weixin.framework.account.WeixinAppService;
import cn.asany.weixin.framework.core.MpCoreHelper;
import cn.asany.weixin.framework.core.WeixinCoreHelper;
import cn.asany.weixin.framework.factory.WeixinSessionFactoryBean;
import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.listener.SubscribeListener;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 微信模块自动配置类
 *
 * @author limaofeng
 */
@Configuration
@EntityScan("cn.asany.weixin.domain")
@ComponentScan({
  "cn.asany.weixin.dao",
  "cn.asany.weixin.service",
  "cn.asany.weixin.graphql",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.weixin.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
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
  public WeixinCoreHelper weixinCoreHelper() {
    return new MpCoreHelper();
  }

  @Bean
  public WeixinSessionFactoryBean weiXinSessionFactoryBean(
      WeixinAppService weixinAppService, WeixinCoreHelper weixinCoreHelper) {
    WeixinSessionFactoryBean weixinSessionFactoryBean = new WeixinSessionFactoryBean();
    weixinSessionFactoryBean.setApplicationContext(applicationContext);

    weixinSessionFactoryBean.setWeixinAppService(weixinAppService);
    weixinSessionFactoryBean.setWeixinCoreHelper(weixinCoreHelper);

    // 关注时,记录粉丝信息
    weixinSessionFactoryBean.addEventListener(
        EventMessage.EventType.subscribe, subscribeListener());

    return weixinSessionFactoryBean;
  }
}
