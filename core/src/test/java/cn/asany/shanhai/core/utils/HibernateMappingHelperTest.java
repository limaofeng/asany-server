package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.service.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("dev")
@Slf4j
class HibernateMappingHelperTest {

    @Autowired
    private ModelService modelService;
    @Autowired
    private HibernateMappingHelper hibernateMappingHelper;

    @Test
    @Transactional
    void generateXML() {
        Optional<Model> model = modelService.get(213L);
        String xml = hibernateMappingHelper.generateXML(model.get());
        System.out.println(xml);
    }
}