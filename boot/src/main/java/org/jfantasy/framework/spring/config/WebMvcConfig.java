package org.jfantasy.framework.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;
import org.hibernate.validator.HibernateValidator;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.jackson.UnirestObjectMapper;
import org.jfantasy.framework.spring.mvc.method.annotation.PagerModelAttributeMethodProcessor;
import org.jfantasy.framework.spring.mvc.method.annotation.PropertyFilterModelAttributeMethodProcessor;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.web.filter.ActionContextFilter;
import org.jfantasy.framework.web.filter.ConversionCharacterEncodingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/** @author limaofeng */
@EnableWebMvc
@Configuration
@ComponentScan(
    basePackages = {"org.jfantasy.*.rest"},
    useDefaultFilters = false,
    includeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ANNOTATION,
          value = {RestController.class, Controller.class})
    })
@Order(value = WebMvcConfig.ORDER)
public class WebMvcConfig implements WebMvcConfigurer {

  public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 32;

  private final ApplicationContext applicationContext;

  private final ObjectMapper objectMapper;
  private final CorsFilter corsFilter;

  @Autowired
  public WebMvcConfig(
      ApplicationContext applicationContext, ObjectMapper objectMapper, CorsFilter corsFilter) {
    this.applicationContext = applicationContext;
    this.objectMapper = objectMapper;
    this.corsFilter = corsFilter;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("*.html").addResourceLocations("/");
  }

  @Bean
  public LocaleResolver localeResolver() {
    return new CookieLocaleResolver();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("lang");
    registry.addInterceptor(localeChangeInterceptor);
  }

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize(DataSize.ofBytes(10485760));
    return factory.createMultipartConfig();
  }

  @PostConstruct
  public void initObjectMapper() {
    JSON.initialize(objectMapper);
    Unirest.setObjectMapper(new UnirestObjectMapper(objectMapper));
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    Class[] removeClazz =
        new Class[] {StringHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class};
    converters.removeIf(converter -> ObjectUtil.exists(removeClazz, converter.getClass()));
    converters.add(0, new MappingJackson2HttpMessageConverter(this.objectMapper));
    converters.add(0, new StringHttpMessageConverter(Charset.forName("utf-8")));
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new PropertyFilterModelAttributeMethodProcessor());
    argumentResolvers.add(new PagerModelAttributeMethodProcessor());
  }

  @Override
  public Validator getValidator() {
    if (applicationContext instanceof XmlWebApplicationContext) {
      ConfigurableApplicationContext configurableApplicationContext =
          (ConfigurableApplicationContext) applicationContext;
      DefaultListableBeanFactory defaultListableBeanFactory =
          (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
      BeanDefinitionBuilder beanDefinitionBuilder =
          BeanDefinitionBuilder.genericBeanDefinition(LocalValidatorFactoryBean.class);
      beanDefinitionBuilder.setAutowireMode(2);
      beanDefinitionBuilder.addPropertyValue("providerClass", HibernateValidator.class);
      defaultListableBeanFactory.registerBeanDefinition(
          "validator", beanDefinitionBuilder.getBeanDefinition());
      return configurableApplicationContext.getBean("validator", Validator.class);
    }
    return null;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    UrlBasedCorsConfigurationSource configSource =
        ClassUtil.getValue(this.corsFilter, "configSource");
    configSource.registerCorsConfiguration("/**", corsConfig());
  }

  @Bean
  public FilterRegistrationBean conversionCharacterEncodingFilter() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(new ConversionCharacterEncodingFilter());
    filterRegistrationBean.setEnabled(true);
    filterRegistrationBean.setOrder(200);
    filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
    filterRegistrationBean.addUrlPatterns("/*");
    return filterRegistrationBean;
  }

  @Bean
  public FilterRegistrationBean actionContextFilter() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(new ActionContextFilter());
    filterRegistrationBean.setEnabled(true);
    filterRegistrationBean.setOrder(300);
    filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
    filterRegistrationBean.addUrlPatterns("/*");
    return filterRegistrationBean;
  }

  private CorsConfiguration corsConfig() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();

    String[] headers =
        new String[] {
          "Accept",
          "Origin",
          "cache-control",
          "x-requested-with",
          "Authorization",
          "Content-Type",
          "Last-Modified"
        };

    String[] methods = new String[] {"GET", "POST", "HEAD", "PATCH", "PUT", "DELETE", "OPTIONS"};

    corsConfiguration.addAllowedOriginPattern("*");
    corsConfiguration.setAllowedHeaders(Arrays.asList(headers));
    corsConfiguration.setAllowedMethods(Arrays.asList(methods));
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setMaxAge(3600L);
    return corsConfiguration;
  }
}
