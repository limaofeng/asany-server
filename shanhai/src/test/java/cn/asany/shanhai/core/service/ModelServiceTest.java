package cn.asany.shanhai.core.service;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.IModelFeature;
import java.util.ArrayList;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class ModelServiceTest {

  @Autowired private ModelService modelService;

  @BeforeEach
  void setUp() {
    //    modelService.clear();
  }

  @AfterEach
  void tearDown() {}

  @Test
  public void contextLoads() {
    modelService.findPage(PageRequest.of(0, 5), new ArrayList<>());
  }

  private Model testEmployee() {
    Model model =
        Model.builder()
            .code("Employee")
            .name("员工")
            .features(IModelFeature.MASTER_MODEL, IModelFeature.SYSTEM_FIELDS)
            .build();
    return modelService.save(model);
  }

  @Test
  public void testCreateModelField() {
    Optional<Model> modelOptional = this.modelService.findByCode("Employee");
    assert modelOptional.isPresent();
    Model model = modelOptional.get();
    this.modelService.addField(
        model.getId(), ModelField.builder().code("name").name("名称").type("String").build());
  }

  @Test
  void clear() {
    log.debug("新增成功");
  }

  @Test
  void save() {
    Model model = testEmployee();
    log.debug("新增成功:" + model.getId());
  }

  @Test
  void update() {
    Model original = this.testEmployee();
    ModelField nameField = ObjectUtil.find(original.getFields(), "code", "name");
    Model model =
        Model.builder()
            .id(original.getId())
            .code("Employee")
            .name("员工")
            .field(nameField.getId(), "name", "名称", FieldType.String)
            .field("age", "年龄", FieldType.Int)
            .features(IModelFeature.MASTER_MODEL)
            .build();
    model = modelService.update(model);
    log.debug(model.getCode());
  }

  @Test
  void publish() {
    Optional<Model> optional = modelService.findByCode("Employee");
    Model model = optional.orElseGet(this::testEmployee);
    modelService.publish(model.getId());
    log.debug("Hibernate HBM XML:" + model.getMetadata().getHbm());
  }
}
