package cn.asany.shanhai.core.service;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.core.bean.ModelDelegate;
import cn.asany.shanhai.core.bean.enums.ModelDelegateType;
import cn.asany.shanhai.core.support.graphql.resolvers.mock.MockGraphQLGetQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class ModelDelegateServiceTest {

    @Autowired
    private ModelDelegateService modelDelegateService;

    @Test
    void save() {
        ModelDelegate GET = ModelDelegate.builder()
            .name("获取单个对象")
            .type(ModelDelegateType.Mock)
            .delegateClassName(MockGraphQLGetQueryResolver.class.getName())
            .build();
        modelDelegateService.save(GET);
    }
}