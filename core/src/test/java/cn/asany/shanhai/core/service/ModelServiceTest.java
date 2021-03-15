package cn.asany.shanhai.core.service;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.support.FieldType;
import cn.asany.shanhai.core.support.features.SystemFieldsFeature;
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
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("dev")
@Slf4j
class ModelServiceTest {

    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelFeatureService modelFeatureService;

    @BeforeEach
    void setUp() {
        modelService.clear();
        modelFeatureService.clear();
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
            .name("员工")
            .features(SystemFieldsFeature.ID)
            .fields(Arrays.asList(ModelField.builder()
                .name("名称")
                .type(FieldType.STRING)
                .build()))
            .build();
        model = modelService.save(model);
        log.debug("新增成功:" + model);
    }

    @Test
    void publish() {
        modelService.publish(223L);
    }
}