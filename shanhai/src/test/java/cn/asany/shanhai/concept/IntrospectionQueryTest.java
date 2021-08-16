package cn.asany.shanhai.concept;

import cn.asany.shanhai.TestApplication;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.graphql.client.GraphQLClient;
import org.jfantasy.graphql.client.GraphQLTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public class IntrospectionQueryTest {

  @GraphQLClient private GraphQLTemplate graphQLTemplate;

  public void introspection() {}
}
