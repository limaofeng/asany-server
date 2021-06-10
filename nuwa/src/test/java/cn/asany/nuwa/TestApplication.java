package cn.asany.nuwa;

import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.actuate.autoconfigure.audit.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: 测试程序入口
 * @date 2019/2/13 4:04 PM
 */
@Slf4j
@Configuration
@ComponentScan("cn.asany.*.*")
@EntityScan({
    "cn.asany.*.*.bean",
})
@EnableJpaRepositories(
    includeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            value = {JpaRepository.class}
        )
    },
    basePackages = {
        "cn.asany.*.*.dao",
    },
    repositoryBaseClass = ComplexJpaRepository.class
)
@EnableAutoConfiguration(exclude = {RestClientAutoConfiguration.class, MongoAutoConfiguration.class, QuartzAutoConfiguration.class, WebMvcAutoConfiguration.class, AuditAutoConfiguration.class})
public class TestApplication {

}