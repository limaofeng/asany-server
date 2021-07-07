package cn.asany.shanhai.core.support.dao;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.utils.HibernateMappingHelper;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Model SessionFactory
 *
 * @author limaofeng
 */
public class ModelSessionFactory implements InitializingBean, ModelRepositoryFactory {

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;
    private MetadataSources metadataSources;
    private HibernateMappingHelper hibernateMappingHelper;

    private Map<Long, String> repositoryEntityNameMap = new ConcurrentHashMap<>();
    private Map<String, ModelRepository> repositoryMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        StandardServiceRegistry serviceRegistry = sessionFactory.getSessionFactoryOptions().getServiceRegistry();
        this.metadataSources = new MetadataSources(serviceRegistry);
        this.hibernateMappingHelper = new HibernateMappingHelper();
        this.hibernateMappingHelper.afterPropertiesSet();
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

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public SessionFactory real() {
        return this.sessionFactory;
    }

    @Override
    public ModelRepository getRepository(String entityName) {
        return this.repositoryMap.get(entityName);
    }

    public ModelRepository buildModelRepository(Model model) {
        ModelRepository repository;
        String xml = hibernateMappingHelper.generateXML(model);
        this.addMetadataSource(xml);
        repositoryEntityNameMap.put(model.getId(), model.getCode());
        repositoryMap.put(model.getCode(), repository = new ModelRepository(model));
        return repository;
    }
}
