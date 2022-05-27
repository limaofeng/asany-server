package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.enums.ModelType;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import cn.asany.shanhai.core.support.graphql.GraphQLServer;
import graphql.GraphQL;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

/**
 * 构建 GraphQL Server
 *
 * @author limaofeng
 */
// @Component
@Profile("!test")
@Slf4j
public class InitModelDaoCommandLineRunner implements CommandLineRunner {

  private final ModelService modelService;
  private final GraphQLServer graphQLServer;
  private final ModelSessionFactory modelSessionFactory;

  public InitModelDaoCommandLineRunner(
      ModelService modelService,
      GraphQLServer graphQLServer,
      ModelSessionFactory modelSessionFactory) {
    this.modelService = modelService;
    this.graphQLServer = graphQLServer;
    this.modelSessionFactory = modelSessionFactory;
  }

  public static String fromNow(long time) {
    long times = System.currentTimeMillis() - time;
    if (times < 1000) {
      return times + "ms";
    }
    times = times / 1000;
    return times + "s";
  }

  @Override
  @Transactional(readOnly = true)
  public void run(String... args) {
    try {
      long start = System.currentTimeMillis();
      List<Model> types =
          modelService.findAll(
              ModelType.SCALAR, ModelType.OBJECT, ModelType.INPUT_OBJECT, ModelType.ENUM);
      System.out.println("耗时:" + fromNow(start));
      start = System.currentTimeMillis();
      List<Model> models = modelService.findAll(ModelType.ENTITY);
      System.out.println("耗时:" + fromNow(start));
      graphQLServer.setTypes(types);

      for (Model model : models) {
        ModelRepository repository = modelSessionFactory.buildModelRepository(model);

        graphQLServer.addModel(model, repository);
      }

      GraphQL server = graphQLServer.buildServer();

      log.debug("Scheme:" + server);

      modelSessionFactory.update();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
