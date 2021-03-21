package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import cn.asany.shanhai.core.utils.HibernateMappingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class InitModelDaoCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelSessionFactory modelSessionFactory;
    @Autowired
    private HibernateMappingHelper hibernateMappingHelper;

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        List<Model> models = modelService.findAll(ModelType.OBJECT);
        for (Model model : models) {
            String xml = hibernateMappingHelper.generateXML(model);
            modelSessionFactory.addMetadataSource(xml);
        }
        modelSessionFactory.update();
    }
}
