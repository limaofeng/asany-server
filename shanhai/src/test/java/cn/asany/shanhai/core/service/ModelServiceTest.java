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
  public void testAccount() {
    Model model =
        Model.builder()
            .code("Account")
            .name("账户")
            .module(0L)
            .field("code", "编码", "single_line_text")
            .field("name", "名称", "single_line_text")
            .features(IModelFeature.MASTER_MODEL, IModelFeature.SYSTEM_FIELDS)
            .build();
    modelService.save(model);
  }

  @Test
  public void deleteAccount() {
    Optional<Model> optionalAccount = this.modelService.findByCode("Account");
    if (!optionalAccount.isPresent()) {
      return;
    }
    modelService.delete(optionalAccount.get().getId());
  }

  @Test
  public void testCreateAccountField() {
    Optional<Model> modelOptional = this.modelService.findByCode("Account");
    assert modelOptional.isPresent();
    Model model = modelOptional.get();
    this.modelService.addField(
        model.getId(),
        ModelField.builder()
            .code("balance")
            .name("余额")
            .type("float")
            .metadata("F_BALANCE")
            .build());
  }

  @Test
  public void deleteAccountField() {
    Optional<Model> optionalAccount = this.modelService.findByCode("Account");
    if (!optionalAccount.isPresent()) {
      return;
    }
    modelService.removeField(optionalAccount.get().getId(), 10512L);
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
  void testUpdateModelField() {
    Optional<Model> modelOptional = this.modelService.findByCode("Employee");
    assert modelOptional.isPresent();
    Model model = modelOptional.get();

    Optional<ModelField> fieldOptional =
        model.getFields().stream().filter(f -> f.getCode().equals("code")).findAny();
    assert fieldOptional.isPresent();
    ModelField field = fieldOptional.get();

    this.modelService.updateField(
        model.getId(),
        field.getId(),
        ModelField.builder().code("name").name("名称").type("String").build());
  }

  @Test
  public void deleteEmployee() {
    Optional<Model> optionalAccount = this.modelService.findByCode("Employee");
    if (!optionalAccount.isPresent()) {
      return;
    }
    modelService.delete(optionalAccount.get().getId());
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
            .code("Account")
            .name("账户")
            .field(nameField.getId(), "name", "名称", FieldType.String)
            .field("age", "年龄", FieldType.Int)
            .features(IModelFeature.MASTER_MODEL)
            .build();
    model = modelService.update(model);
    log.debug(model.getCode());
  }
}
