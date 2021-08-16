package cn.asany.shanhai.core.graphql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import cn.asany.shanhai.TestApplication;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class ModelGraphQLMutationResolverTest {

  @Autowired private GraphQLTestTemplate graphQLTestTemplate;

  @Test
  void createModel() throws IOException {
    GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/create_model.graphql");
    assertNotNull(response);
    assertThat(response.isOk()).isTrue();
    assertThat(response.get("$.data.users.pageSize", Integer.class)).isEqualTo(15);
  }
}
