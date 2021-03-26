package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.bean.ModelDelegate;
import cn.asany.shanhai.core.bean.enums.ModelDelegateType;
import cn.asany.shanhai.core.service.ModelDelegateService;
import cn.asany.shanhai.core.support.graphql.resolvers.base.BaseQueryGetDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("#{!environment.getProperty(\"spring.profiles.active\").contains(\"test\")}")
public class DefaultCRUDDelegateCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ModelDelegateService modelDelegateService;

    @Override
    public void run(String... args) throws Exception {
        ModelDelegate GET = ModelDelegate.builder()
            .name("获取单个对象")
            .type(ModelDelegateType.Base)
            .delegateClassName(BaseQueryGetDataFetcher.class.getName())
            .build();
        modelDelegateService.save(GET);
    }

}
