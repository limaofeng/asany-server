package cn.asany.shanhai.core.service;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.runners.PresetModelCommandLineRunner;
import cn.asany.shanhai.core.runners.PresetModelFeatureCommandLineRunner;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.IModelFeature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.Pager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Slf4j
class ModelServiceTest {

    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelFeatureService modelFeatureService;
    @Autowired
    private PresetModelFeatureCommandLineRunner modelFeatureCommandLineRunner;
    @Autowired
    private PresetModelCommandLineRunner modelCommandLineRunner;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        modelFeatureService.clear();
        modelService.clear();
        modelFeatureCommandLineRunner.run();
        modelCommandLineRunner.run();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void contextLoads() {
        modelService.findPager(new Pager<>(), new ArrayList<>());
    }


    @Test
    void save() {
        Model model = Model.builder()
            .code("Employee")
            .name("员工")
            .features(IModelFeature.MASTER_MODEL, IModelFeature.SYSTEM_FIELDS)
            .fields(ModelField.builder()
                .code("name")
                .name("名称")
                .type(FieldType.String)
                .build())
            .build();
        model = modelService.save(model);
        log.debug("新增成功:" + model);
    }

    @Test
    void publish() {
        this.save();
        Pager<Model> pager = modelService.findPager(new Pager<>(), new ArrayList<>());
        Model model = pager.getPageItems().stream().findFirst().get();
        modelService.publish(model.getId());

        model = modelService.get(model.getId()).get();
        log.debug("Hibernate HBM XML:" + model.getMetadata().getHbm());
    }

}