package cn.asany.shanhai.core.support.dao;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.utils.HibernateMappingHelper;
import jakarta.persistence.EntityManagerFactory;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.internal.SessionFactoryOptionsBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator;
import org.jfantasy.framework.util.FantasyClassLoader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

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

  @Autowired private EntityManagerFactory entityManagerFactory;
  private SessionFactory sessionFactory;
  private MetadataSources metadataSources;
  private HibernateMappingHelper hibernateMappingHelper;
  private StandardServiceRegistry serviceRegistry;

  private final Map<String, ModelRepository> repositoryMap = new ConcurrentHashMap<>();

  @Override
  public void afterPropertiesSet() throws Exception {
    this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    this.serviceRegistry = sessionFactory.getSessionFactoryOptions().getServiceRegistry();
    this.metadataSources = new MetadataSources(serviceRegistry);
    this.hibernateMappingHelper = new HibernateMappingHelper();
    this.hibernateMappingHelper.afterPropertiesSet();
  }

  public void addMetadataSource(String xml) {
    metadataSources.addInputStream(new ByteArrayInputStream(xml.getBytes()));
    Metadata metadata = metadataSources.buildMetadata();

    // 使用 SchemaManagementToolCoordinator 执行 schema 管理
    SchemaManagementToolCoordinator.process(
      metadata,
      this.serviceRegistry,
      this.serviceRegistry.getService(ConfigurationService.class).getSettings(),
      (target) -> EnumSet.of(TargetType.DATABASE)
    );
  }

  public void update() {
    Metadata metadata = metadataSources.buildMetadata();
    sessionFactory = metadata.buildSessionFactory();
    SessionFactoryOptionsBuilder sessionFactoryOptions =
        (SessionFactoryOptionsBuilder) sessionFactory.getSessionFactoryOptions();
    sessionFactoryOptions.applyInterceptor(new SystemFieldFillInterceptor());

    StandardServiceRegistry serviceRegistry =
        sessionFactory.getSessionFactoryOptions().getServiceRegistry();
    this.metadataSources = new MetadataSources(serviceRegistry);
  }

  public Session getCurrentSession() {
    return sessionFactory.getCurrentSession();
  }

  @Override
  public ModelRepository getRepository(String entityName) {
    return this.repositoryMap.get(entityName);
  }

  @SneakyThrows
  public ModelRepository buildModelRepository(Model model) {
    ModelRepository repository;
    String entityClassName = model.getModule().getCode().concat(".").concat(model.getCode());
    String xml = hibernateMappingHelper.generateXML(model);
    this.addMetadataSource(xml);
    Class<?> entityClass = FantasyClassLoader.getClassLoader().loadClass(entityClassName);
    repositoryMap.put(model.getCode(), repository = new ModelRepository(model, this, entityClass));
    return repository;
  }

  public void unbuildModelRepository(String code) {
    this.repositoryMap.remove(code);
  }

  public SessionFactory real() {
    return this.sessionFactory;
  }
}
