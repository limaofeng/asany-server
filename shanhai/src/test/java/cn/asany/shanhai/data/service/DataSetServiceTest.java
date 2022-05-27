package cn.asany.shanhai.data.service;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.data.domain.DataSetConfig;
import cn.asany.shanhai.data.domain.DataSourceConfig;
import cn.asany.shanhai.data.engine.graphql.GraphQLDataSetOptions;
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
class DataSetServiceTest {

  @Autowired private DataSetService dataSetService;

  @BeforeEach
  void setUp() {
    this.delete();
  }

  @AfterEach
  void tearDown() {}

  @Test
  void save() {
    DataSetConfig config =
        DataSetConfig.builder()
            .id(1L)
            .datasource(DataSourceConfig.builder().id(1L).build())
            .name("文章栏目")
            .options(
                GraphQLDataSetOptions.builder()
                    .operationName("articleChannels")
                    .gql(
                        "query articleChannels($parent: ID) {\n"
                            + "      articleChannels(filter: { parent: $parent, descendant: true }) {\n"
                            + "        id\n"
                            + "        name\n"
                            + "        parent {\n"
                            + "          id\n"
                            + "        }\n"
                            + "        path\n"
                            + "      }\n"
                            + "    }")
                    .build())
            .build();
    dataSetService.save(config);
  }

  @Test
  void update() {}

  @Test
  void delete() {
    this.dataSetService.delete(1L);
  }

  @Test
  void getConfig() {}
}
