package cn.asany.shanhai.core.support.model;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;
import java.io.ByteArrayInputStream;
import java.util.EnumSet;

public class RuntimeMetadataRegistry implements InitializingBean {

    @Autowired(required = false)
    private EntityManagerFactory entityManagerFactory;

    private MetadataSources metadataSources;

    @Override
    public void afterPropertiesSet() throws Exception {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        StandardServiceRegistry serviceRegistry = sessionFactory.getSessionFactoryOptions().getServiceRegistry();
        this.metadataSources = new MetadataSources(serviceRegistry);
        sessionFactory.getSessionFactoryOptions();
    }

    public Metadata addMapping(String xml) {
        metadataSources.addInputStream(new ByteArrayInputStream(xml.getBytes()));
        Metadata metadata = metadataSources.buildMetadata();
        //创建数据库Schema,如果不存在就创建表,存在就更新字段,不会影响已有数据
        SchemaExport schemaExport = new SchemaExport();
        schemaExport.createOnly(EnumSet.of(TargetType.DATABASE), metadata);

        return metadataSources.buildMetadata();
    }
}
