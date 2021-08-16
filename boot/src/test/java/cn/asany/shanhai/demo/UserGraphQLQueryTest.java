package cn.asany.shanhai.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import cn.asany.shanhai.TestApplication;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class UserGraphQLQueryTest {

  @Autowired private GraphQLTestTemplate graphQLTestTemplate;

  @Test
  public void get_users() throws IOException {
    GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/users.graphql");
    assertNotNull(response);
    assertThat(response.isOk()).isTrue();
    assertThat(response.get("$.data.users.pageSize", int.class)).isEqualTo(15);
  }
}
