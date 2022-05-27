package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.enums.ModelType;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.dao.ManualTransactionManager;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.junit.jupiter.api.BeforeEach;
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
class InitModelDaoCommandLineRunnerTest {

  @Autowired private ModelService modelService;
  @Autowired private ModelSessionFactory sessionFactory;

  @BeforeEach
  void setUp() {}

  @Test
  @Transactional
  void run() {
    List<Model> models = modelService.findAll(ModelType.ENTITY);
    for (Model model : models) {
      sessionFactory.buildModelRepository(model);
    }
    sessionFactory.update();

    ManualTransactionManager transactionManager = new ManualTransactionManager(sessionFactory);
    transactionManager.bindSession();

    ModelRepository modelJpaRepository = sessionFactory.getRepository("Employee");
    PropertyFilterBuilder builder = PropertyFilter.builder();

    builder.or(
        PropertyFilter.builder().equal("name", "王武ds11").isNull("createdAt"),
        PropertyFilter.builder().startsWith("name", "12312"));

    List result = modelJpaRepository.findAll(builder.build());
    System.out.println("resultList: " + result);

    transactionManager.unbindSession();

    //        Session session = sessionFactory.getCurrentSession();
    //
    //        //关闭会话
    //        System.out.println("session: " + session);
    //        Query query = session.createQuery("from Employee");
    //        List list = query.getResultList();
    //        System.out.println("resultList: " + list);
  }
}
