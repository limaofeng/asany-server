package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.bean.ModelFeature;
import cn.asany.shanhai.core.service.ModelFeatureService;
import cn.asany.shanhai.core.support.features.SystemFieldsFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PresetModelFeatureCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ModelFeatureService modelFeatureService;

    @Override
    public void run(String... args) throws Exception {
        ModelFeature systemFields = ModelFeature.builder().id(SystemFieldsFeature.ID).name("系统字段").build();
        modelFeatureService.save(systemFields);
    }
}
