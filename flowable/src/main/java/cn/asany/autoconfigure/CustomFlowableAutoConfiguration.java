package cn.asany.autoconfigure;

import org.flowable.ui.common.rest.idm.remote.RemoteAccountResource;
import org.flowable.ui.common.service.idm.RemoteIdmServiceImpl;
import org.flowable.ui.modeler.conf.SecurityConfiguration;
import org.flowable.ui.modeler.properties.FlowableModelerAppProperties;
import org.flowable.ui.modeler.rest.app.EditorGroupsResource;
import org.flowable.ui.modeler.rest.app.EditorUsersResource;
import org.flowable.ui.modeler.rest.app.StencilSetResource;
import org.flowable.ui.modeler.security.RemoteIdmAuthenticationProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(FlowableModelerAppProperties.class)
@ComponentScan( //
    basePackages = { //
      "cn.asany.flowable.core.graphql",
      "cn.asany.flowable.core.service",
      "org.flowable.ui.modeler.conf", //
      "org.flowable.ui.modeler.repository", //
      "org.flowable.ui.modeler.service", //
      "org.flowable.ui.modeler.security", //
      "org.flowable.ui.common.filter", //
      "org.flowable.ui.common.service", //
      "org.flowable.ui.common.repository", //
      "org.flowable.ui.common.security", //
      "org.flowable.ui.common.tenant", //
      "org.flowable.ui.modeler.rest.app", //
      "org.flowable.ui.common.rest",
    }, //
    excludeFilters = {
      // 移除flowable.common.app 的设置
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = EditorUsersResource.class), //
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = EditorGroupsResource.class), //
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RemoteIdmServiceImpl.class), //
      @Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = RemoteIdmAuthenticationProvider.class), //
      // 移除flowable 中的spring security 的设置
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class), //
      @Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = SecurityConfiguration.ApiWebSecurityConfigurationAdapter.class), //
      @Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = SecurityConfiguration.ActuatorWebSecurityConfigurationAdapter.class), //
      @Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = SecurityConfiguration.FormLoginWebSecurityConfigurerAdapter.class), //
      // 编辑器国际化文件
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = StencilSetResource.class), //
      @Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = org.flowable.ui.modeler.conf.ApplicationConfiguration.class), //
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RemoteAccountResource.class) //
    } //
    )
@Import(WebSecurityConfig.class)
public class CustomFlowableAutoConfiguration {}
