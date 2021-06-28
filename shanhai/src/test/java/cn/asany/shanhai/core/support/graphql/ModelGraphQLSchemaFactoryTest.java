package cn.asany.shanhai.core.support.graphql;

import cn.asany.shanhai.TestApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("dev")
@Slf4j
class ModelGraphQLSchemaFactoryTest {

    @Autowired
    private GraphQLServer graphQLServer;

    @Test
    void buildScheme() {
        String scheme = graphQLServer.buildScheme();
        log.debug("Scheme: " + scheme);
    }
}