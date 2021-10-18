package cn.asany.autoconfigure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan({"cn.asany.organization.*.bean"})
public class OrganizationAutoConfiguration {}
