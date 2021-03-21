package cn.asany.shanhai.core.support.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.internal.SessionFactoryOptionsBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;
import java.io.ByteArrayInputStream;
import java.util.EnumSet;

/**
 * Model SessionFactory
 * @author limaofeng
 */
public class ModelSessionFactory implements InitializingBean {

    @Autowired(required = false)
    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;
    private MetadataSources metadataSources;

    @Override
    public void afterPropertiesSet() throws Exception {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        StandardServiceRegistry serviceRegistry = sessionFactory.getSessionFactoryOptions().getServiceRegistry();
        this.metadataSources = new MetadataSources(serviceRegistry);
        sessionFactory.getSessionFactoryOptions();
    }

    public void addMetadataSource(String xml) {
        metadataSources.addInputStream(new ByteArrayInputStream(xml.getBytes()));
        Metadata metadata = metadataSources.buildMetadata();
        //创建数据库Schema,如果不存在就创建表,存在就更新字段,不会影响已有数据
        SchemaExport schemaExport = new SchemaExport();
        schemaExport.createOnly(EnumSet.of(TargetType.DATABASE), metadata);
    }

    public void update() {
        Metadata metadata = metadataSources.buildMetadata();
        sessionFactory = metadata.buildSessionFactory();
        SessionFactoryOptionsBuilder sessionFactoryOptions = (SessionFactoryOptionsBuilder) sessionFactory.getSessionFactoryOptions();
        sessionFactoryOptions.applyInterceptor(new SystemFieldFillInterceptor());
    }

    public Session openSession() throws HibernateException {
        return sessionFactory.openSession();
    }

}
