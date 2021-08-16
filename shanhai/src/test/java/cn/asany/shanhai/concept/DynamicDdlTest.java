package cn.asany.shanhai.concept;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.core.support.dao.SystemFieldFillInterceptor;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.internal.SessionFactoryOptionsBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.query.Query;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class DynamicDdlTest {

  @Autowired private EntityManagerFactory entityManagerFactory;

  //    @Autowired
  //    private RuntimeMetadataRegistry runtimeMetadataRegistry;
  /**
   * 运行期的持久化实体没有必要一定表示为像POJO类或JavaBean对象那样的形式。 Hibernate也支持动态模型在运行期使用Map）和象DOM4J的树模型那样的实体表示。
   * 使用这种方法，你不用写持久化类，只写映射文件就行了。
   */
  public static final String XML_MAPPING =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
          + "<!DOCTYPE hibernate-mapping PUBLIC\n"
          + "        \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n"
          + "        \"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\">\n"
          + "<hibernate-mapping>\n"
          + "    <class entity-name=\"Student\" table=\"t_student\">\n"
          + "        <id name=\"id\" type=\"java.lang.Long\" length=\"64\" unsaved-value=\"null\">\n"
          + "            <generator class=\"fantasy-sequence\" />\n"
          + "        </id>"
          + "        <property type=\"java.lang.String\" name=\"username\" column=\"username\"/>\n"
          + "        <property name=\"password\" type=\"java.lang.String\" column=\"password\"/>\n"
          + "        <property name=\"sex\" type=\"java.lang.String\" column=\"sex\"/>\n"
          + "        <property name=\"age\" type=\"java.lang.Integer\" column=\"age\"/>\n"
          + "        <property name=\"birthday\" type=\"java.util.Date\" column=\"birthday\"/>\n"
          + "    </class>"
          + "</hibernate-mapping>";

  @Test
  public void testDynamicDdl() {
    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    StandardServiceRegistry serviceRegistry =
        sessionFactory.getSessionFactoryOptions().getServiceRegistry();
    MetadataSources metadataSources = new MetadataSources(serviceRegistry);
    metadataSources.addInputStream(new ByteArrayInputStream(XML_MAPPING.getBytes()));
    Metadata metadata = metadataSources.buildMetadata();
    // 读取映射文件

    // 创建会话工厂
    SessionFactory newSessionFactory = metadata.buildSessionFactory();
    SessionFactoryOptionsBuilder sessionFactoryOptions =
        (SessionFactoryOptionsBuilder) newSessionFactory.getSessionFactoryOptions();
    sessionFactoryOptions.applyInterceptor(new SystemFieldFillInterceptor());
    // 保存对象
    Session newSession = newSessionFactory.openSession();
    Transaction transaction = newSession.beginTransaction();
    for (int i = 0; i < 100; i++) {
      //            Student student = Student.builder()
      //                .username("张三" + i)
      //                .password("adsfwr" + i)
      //                .sex(i % 2 == 0 ? "male" : "female")
      //                .age(i).
      //                    birthday(new Date())
      //                .build();
      Map<String, Object> student = new HashMap<>();
      student.put("username", "张三" + i);
      student.put("password", "adsfwr" + i);
      student.put("sex", i % 2 == 0 ? "male" : "female");
      student.put("age", i);
      student.put("birthday", new Date());
      newSession.save("Student", student);
    }
    transaction.commit();
    // 查询所有对象
    Query query = newSession.createQuery("from Student");
    List list = query.getResultList();
    System.out.println("resultList: " + list);
    // 关闭会话
    newSession.close();
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  @Builder
  static class Student extends BaseBusEntity {
    private String username;
    private String password;
    private String sex;
    private Integer age;
    private Date birthday;
  }
}
