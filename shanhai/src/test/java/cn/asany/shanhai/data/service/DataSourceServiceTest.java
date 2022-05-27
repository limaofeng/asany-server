package cn.asany.shanhai.data.service;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.data.domain.DataSourceConfig;
import cn.asany.shanhai.data.engine.graphql.GraphQLDataSourceBuilder;
import cn.asany.shanhai.data.engine.graphql.GraphQLDataSourceOptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
class DataSourceServiceTest {

  @Autowired private DataSourceService dataSourceService;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void getConfig() {}

  @Test
  void save() {
    DataSourceConfig config =
        DataSourceConfig.builder()
            .id(1L)
            .name("默认 GraphQL 网关")
            .type(GraphQLDataSourceBuilder.GRAPHQL)
            .options(
                GraphQLDataSourceOptions.builder().url("http://localhost:8080/graphql").build())
            .build();
    dataSourceService.save(config);
  }

  @Test
  void update() {}

  @Test
  void delete() {}
}
