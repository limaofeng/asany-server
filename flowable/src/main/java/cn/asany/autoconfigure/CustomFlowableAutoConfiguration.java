package cn.asany.autoconfigure;

import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.ui.common.rest.idm.remote.RemoteAccountResource;
import org.flowable.ui.common.service.exception.InternalServerErrorException;
import org.flowable.ui.common.service.idm.RemoteIdmServiceImpl;
import org.flowable.ui.modeler.conf.DatabaseConfiguration;
import org.flowable.ui.modeler.conf.SecurityConfiguration;
import org.flowable.ui.modeler.properties.FlowableModelerAppProperties;
import org.flowable.ui.modeler.rest.app.EditorGroupsResource;
import org.flowable.ui.modeler.rest.app.EditorUsersResource;
import org.flowable.ui.modeler.rest.app.StencilSetResource;
import org.flowable.ui.modeler.security.RemoteIdmAuthenticationProvider;
import org.jfantasy.framework.dao.mybatis.ConfigurationPropertiesCustomizer;
import org.jfantasy.framework.spring.config.MyBatisConfig;
import org.jfantasy.framework.util.common.ClassUtil;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;

@Slf4j
@Configuration
@AutoConfigureBefore(MybatisAutoConfiguration.class)
@EnableConfigurationProperties({MybatisProperties.class, FlowableModelerAppProperties.class})
@ComponentScan( //
    basePackages = { //
      "cn.asany.flowable.core.graphql",
      "cn.asany.flowable.core.service",
      "org.flowable.ui.modeler.conf", //
      "org.flowable.ui.modeler.repository", //
      "org.flowable.ui.modeler.service", //
      "org.flowable.ui.modeler.security", //
      "org.flowable.ui.common.filter", //
      "org.flowable.ui.common.service", //
      "org.flowable.ui.common.repository", //
      "org.flowable.ui.common.security", //
      "org.flowable.ui.common.tenant", //
      "org.flowable.ui.modeler.rest.app", //
      "org.flowable.ui.common.rest",
    }, //
    excludeFilters = {
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = DatabaseConfiguration.class),
      // 移除flowable.common.app 的设置
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = EditorUsersResource.class), //
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = EditorGroupsResource.class), //
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RemoteIdmServiceImpl.class), //
      @Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = RemoteIdmAuthenticationProvider.class), //
      // 移除flowable 中的spring security 的设置
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class), //
      @Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = SecurityConfiguration.ApiWebSecurityConfigurationAdapter.class), //
      @Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = SecurityConfiguration.ActuatorWebSecurityConfigurationAdapter.class), //
      @Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = SecurityConfiguration.FormLoginWebSecurityConfigurerAdapter.class), //
      // 编辑器国际化文件
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = StencilSetResource.class), //
      @Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = org.flowable.ui.modeler.conf.ApplicationConfiguration.class), //
      @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RemoteAccountResource.class) //
    } //
    )
@Import(WebSecurityConfig.class)
public class CustomFlowableAutoConfiguration {

  protected static final String LIQUIBASE_CHANGELOG_PREFIX = "ACT_DE_";

  @Bean("asany.flowable.configurationPropertiesCustomizer")
  public ConfigurationPropertiesCustomizer mybatisConfigurationCustomizer(
      FlowableModelerAppProperties modelerAppProperties) {
    return properties -> {
      properties.put("prefix", modelerAppProperties.getDataSourcePrefix());
      properties.put("blobType", "BLOB");
      properties.put("boolValue", "TRUE");
    };
  }

  @Bean
  public Liquibase liquibase(MyBatisConfig myBatisConfig) {
    DataSource dataSource =
        ClassUtil.getFieldValue(myBatisConfig, MyBatisConfig.class, "dataSource");

    log.info("Configuring Liquibase");

    Liquibase liquibase = null;
    try {
      DatabaseConnection connection = new JdbcConnection(dataSource.getConnection());
      Database database =
          DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);
      database.setDatabaseChangeLogTableName(
          LIQUIBASE_CHANGELOG_PREFIX + database.getDatabaseChangeLogTableName());
      database.setDatabaseChangeLogLockTableName(
          LIQUIBASE_CHANGELOG_PREFIX + database.getDatabaseChangeLogLockTableName());

      liquibase =
          new Liquibase(
              "META-INF/liquibase/flowable-modeler-app-db-changelog.xml",
              new ClassLoaderResourceAccessor(),
              database);
      liquibase.update("flowable");
      return liquibase;

    } catch (Exception e) {
      throw new InternalServerErrorException("Error creating liquibase database", e);
    } finally {
      closeDatabase(liquibase);
    }
  }

  private void closeDatabase(Liquibase liquibase) {
    if (liquibase != null) {
      Database database = liquibase.getDatabase();
      if (database != null) {
        try {
          database.close();
        } catch (DatabaseException e) {
          log.warn("Error closing database", e);
        }
      }
    }
  }
}
