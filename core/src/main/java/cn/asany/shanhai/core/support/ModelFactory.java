package cn.asany.shanhai.core.support;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.dao.ModelRepositoryFactory;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import cn.asany.shanhai.core.support.graphql.GraphQLServer;
import cn.asany.shanhai.core.utils.HibernateMappingHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ModelFactory {
    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelSessionFactory modelSessionFactory;
    @Autowired
    private HibernateMappingHelper hibernateMappingHelper;

    private ModelSessionFactory sessionFactory;

    private ModelRepositoryFactory repositoryFactory;

    private GraphQLServer graphQLServer;

    public void loadDefaultModel() {
        List<Model> models = modelService.findAll(ModelType.OBJECT);
        for (Model model : models) {
            String xml = hibernateMappingHelper.generateXML(model);
            modelSessionFactory.addMetadataSource(xml);
        }
        modelSessionFactory.update();
    }

    protected void install() {

    }

    public ModelRepository getRepository(String modelCode) {
        return repositoryFactory.getRepository(modelCode);
    }

}
