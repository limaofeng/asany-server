package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.autoconfigure.ModelAutoConfiguration;
import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@ConditionalOnExpression("#{!environment.getProperty(\"spring.profiles.active\").contains(\"test\")}")
public class InitModelDaoCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ModelAutoConfiguration configuration;

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        configuration.load();
    }
}
