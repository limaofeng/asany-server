package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import cn.asany.shanhai.core.support.graphql.GraphQLServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("!test")
@Slf4j
public class InitModelDaoCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ModelService modelService;
    @Autowired
    private GraphQLServer graphQLServer;
    @Autowired
    private ModelSessionFactory modelSessionFactory;

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
//        long start = System.currentTimeMillis();
//        List<Model> types = modelService.findAll(ModelType.SCALAR, ModelType.OBJECT, ModelType.INPUT_OBJECT, ModelType.ENUM);
//        System.out.println("耗时:" + fromNow(start));
//        start = System.currentTimeMillis();
//        List<Model> models = modelService.findAll(ModelType.ENTITY);
//        System.out.println("耗时:" + fromNow(start));
//        graphQLServer.setTypes(types);
//
//        for (Model model : models) {
//            ModelRepository repository = modelSessionFactory.buildModelRepository(model);
//
//            graphQLServer.addModel(model, repository);
//        }
//
//        GraphQL graphQL = graphQLServer.buildServer();
//
//        log.debug("Scheme:" + graphQL);
//
//        modelSessionFactory.update();
    }
}
