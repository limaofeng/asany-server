package cn.asany.shanhai.data.engine;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.data.domain.DataSetConfig;
import cn.asany.shanhai.data.service.DataSetService;
import java.util.HashMap;
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
class IDataSourceLoaderTest {
  @Autowired private DataSetService dataSetService;
  @Autowired private IDataSourceLoader dataSourceLoader;

  @Test
  void load() {
    DataSetConfig config = dataSetService.getConfig(1L);
    IDataSource dataSource = dataSourceLoader.load(config.getDatasource().getId());
    assert dataSource != null;
    dataSource.dataset(config, new HashMap<>());
  }
}
