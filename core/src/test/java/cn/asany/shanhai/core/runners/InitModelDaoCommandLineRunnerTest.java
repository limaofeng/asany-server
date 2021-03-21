package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.TestApplication;
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
        Session newSession = sessionFactory.openSession();
        Query query = newSession.createQuery("from Employee");
        List list = query.getResultList();
        System.out.println("resultList: " + list);
        //关闭会话
        newSession.close();
    }
}