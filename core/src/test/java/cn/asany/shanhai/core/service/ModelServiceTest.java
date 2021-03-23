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
import org.jfantasy.framework.util.common.ObjectUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
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


    private Model testEmployee() {
        Model model = Model.builder()
            .code("Employee")
            .name("员工")
            .field("name", "名称", FieldType.String)
            .features(IModelFeature.MASTER_MODEL, IModelFeature.SYSTEM_FIELDS)
            .build();
        return modelService.save(model);
    }

    @Test
    void save() {
        Model model = testEmployee();
        log.debug("新增成功:" + model);
    }

    @Test
    void update() {
        Model original = this.testEmployee();
        ModelField nameField = ObjectUtil.find(original.getFields(), "code", "name");
        Model model = Model.builder()
            .id(original.getId())
            .code("Employee")
            .name("员工")
            .field(nameField.getId(), "name", "名称", FieldType.String)
            .field("age", "年龄", FieldType.Int)
            .features(IModelFeature.MASTER_MODEL)
            .build();
        model = modelService.update(model);
    }

    @Test
    void publish() {
        Optional<Model> optional = modelService.findByCode("Employee");
        Model model = optional.orElseGet(() -> this.testEmployee());
        modelService.publish(model.getId());
        log.debug("Hibernate HBM XML:" + model.getMetadata().getHbm());
    }

}