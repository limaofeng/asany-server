package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.core.support.dao.ManualTransactionManager;
import cn.asany.shanhai.core.support.dao.ModelJpaRepository;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Slf4j
class InitModelDaoCommandLineRunnerTest {

    @Autowired
    private ModelSessionFactory sessionFactory;

    @BeforeEach
    void setUp() {
    }

    @Test
    void run() {
        ManualTransactionManager transactionManager = new ManualTransactionManager(sessionFactory.real());
        transactionManager.bindSession();

        ModelJpaRepository modelJpaRepository = new ModelJpaRepository("Employee", sessionFactory);
        List result = modelJpaRepository.findBy("name", "1234");
        System.out.println("resultList: " + result);

        Session session = sessionFactory.getCurrentSession();
        //关闭会话
        System.out.println("session: " + session);
        Query query = session.createQuery("from Employee");
        List list = query.getResultList();
        System.out.println("resultList: " + list);

        transactionManager.unbindSession();
    }
}