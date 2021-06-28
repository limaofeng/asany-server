package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.bean.ModelFeature;
import cn.asany.shanhai.core.service.ModelFeatureService;
import cn.asany.shanhai.core.support.model.IModelFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * 初始化 Feature
 *
 * @author limaofeng
 */
@Component
@ConditionalOnExpression("#{!environment.getProperty(\"spring.profiles.active\").contains(\"test\")}")
public class PresetModelFeatureCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ModelFeatureService modelFeatureService;

    @Override
    public void run(String... args) {
        ModelFeature systemFields = ModelFeature.builder().id(IModelFeature.SYSTEM_FIELDS).name("系统字段").build();
        modelFeatureService.saveOrUpdate(systemFields);
        ModelFeature masterModel = ModelFeature.builder().id(IModelFeature.MASTER_MODEL).name("主表").build();
        modelFeatureService.saveOrUpdate(masterModel);
    }
}
