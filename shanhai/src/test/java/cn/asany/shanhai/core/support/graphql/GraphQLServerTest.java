package cn.asany.shanhai.core.support.graphql;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.autoconfigure.ModelAutoConfiguration;
import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = {TestApplication.class, RestTemplate.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class GraphQLServerTest {

  @Autowired private ModelService modelService;
  @Autowired private ModelSessionFactory modelSessionFactory;
  @Autowired private GraphQLServer graphQLServer;
  @Autowired private ModelAutoConfiguration configuration;

  @Test
  @Transactional
  void buildScheme() {
    List<Model> types =
        modelService.findAll(
            ModelType.SCALAR, ModelType.OBJECT, ModelType.INPUT_OBJECT, ModelType.ENUM);
    List<Model> models = modelService.findAll(ModelType.ENTITY);

    graphQLServer.setTypes(types);

    for (Model model : models) {
      ModelRepository repository = modelSessionFactory.buildModelRepository(model);
      graphQLServer.addModel(model, repository);
    }

    modelSessionFactory.update();
    String scheme = graphQLServer.buildScheme();
    log.debug("SCHEME:" + scheme);
  }

  @Test
  @Transactional
  void buildServer() {}
}
