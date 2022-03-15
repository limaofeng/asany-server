package cn.asany.autoconfigure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan({"cn.asany.drive.bean"})
@ComponentScan({
  "cn.asany.drive.dao",
  "cn.asany.drive.graphql",
  "cn.asany.drive.service",
})
public class DriveAutoConfiguration {}
