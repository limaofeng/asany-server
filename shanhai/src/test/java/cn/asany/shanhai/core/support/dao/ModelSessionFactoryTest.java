package cn.asany.shanhai.core.support.dao;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.core.service.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class ModelSessionFactoryTest {
  @Autowired private ModelService modelService;
  @Autowired private ModelSessionFactory modelSessionFactory;

  @Test
  @Transactional
  void buildRepository() {
    //    Optional<Model> optional = modelService.findByCode("Employee");
    //
    //    Model model = optional.get();
    //
    //    modelSessionFactory.buildModelRepository(model);
    //    modelSessionFactory.update();
    //
    //    ModelRepository repository = modelSessionFactory.buildModelRepository(model);
    //
    //    ManualTransactionManager transactionManager = new
    // ManualTransactionManager(modelSessionFactory);
    //    transactionManager.bindSession();
    //
    //    transactionManager.beginTransaction();
    //
    //    Map<String, Object> employee = new HashMap<>();
    //    employee.put("name", "张三");
    //
    //    repository.save(employee);
    //
    //    transactionManager.commitTransaction();
  }
}
