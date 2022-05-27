package cn.asany.shanhai.data.engine.graphql;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.data.domain.DataSetConfig;
import cn.asany.shanhai.data.engine.DataSet;
import cn.asany.shanhai.data.engine.IDataSource;
import cn.asany.shanhai.data.engine.IDataSourceLoader;
import cn.asany.shanhai.data.service.DataSetService;
import java.util.HashMap;
import java.util.Map;
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
class GraphqlDataSourceTest {

  @Autowired private DataSetService dataSetService;
  @Autowired private IDataSourceLoader dataSourceLoader;

  @Test
  void dataset() {
    DataSetConfig config = dataSetService.getConfig(1L);
    IDataSource dataSource = dataSourceLoader.load(config.getDatasource().getId());
    assert dataSource != null;
    Map<String, String> variables = new HashMap<>();
    variables.put("parent", "13");
    DataSet dataSet = dataSource.dataset(config, variables);
    System.out.println(dataSet);
  }
}
