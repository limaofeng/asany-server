package cn.asany.autoconfigure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置
 *
 * @author limaofeng
 */
@Configuration
@EntityScan({"cn.asany.organization.*.bean"})
@ComponentScan("cn.asany.organization.*.converter")
public class OrganizationAutoConfiguration {}
