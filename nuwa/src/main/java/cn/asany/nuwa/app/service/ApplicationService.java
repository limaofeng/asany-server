package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.bean.ApplicationRoute;
import cn.asany.nuwa.app.bean.ClientSecret;
import cn.asany.nuwa.app.bean.Routespace;
import cn.asany.nuwa.app.bean.enums.ApplicationType;
import cn.asany.nuwa.app.converter.ApplicationConverter;
import cn.asany.nuwa.app.dao.ApplicationDao;
import cn.asany.nuwa.app.dao.ApplicationRouteDao;
import cn.asany.nuwa.app.dao.ClientSecretDao;
import cn.asany.nuwa.app.dao.RoutespaceDao;
import cn.asany.nuwa.app.service.dto.NativeApplication;
import cn.asany.nuwa.app.service.dto.OAuthApplication;
import cn.asany.nuwa.template.bean.ApplicationTemplateRoute;
import cn.asany.security.oauth.dao.AccessTokenDao;
import cn.asany.ui.resources.bean.Component;
import cn.asany.ui.resources.bean.enums.ComponentScope;
import cn.asany.ui.resources.dao.ComponentDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.oauth2.core.ClientDetails;
import org.jfantasy.framework.security.oauth2.core.ClientDetailsService;
import org.jfantasy.framework.security.oauth2.core.ClientRegistrationException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Service;

/**
 * 应用服务
 *
 * @author limaofeng
 */
@Service
public class ApplicationService implements ClientDetailsService {

  private static final String NONCE_CHARS = "abcdef0123456789";

  private final AccessTokenDao accessTokenDao;
  private final ApplicationDao applicationDao;
  private final ClientSecretDao clientSecretDao;
  private final ApplicationRouteDao applicationRouteDao;
  private final ComponentDao componentDao;
  private final RoutespaceDao routespaceDao;
  private final ApplicationConverter applicationConverter;

  public ApplicationService(
      ApplicationDao applicationDao,
      ClientSecretDao clientSecretDao,
      ComponentDao componentDao,
      RoutespaceDao routespaceDao,
      ApplicationConverter applicationConverter,
      ApplicationRouteDao applicationRouteDao,
      AccessTokenDao accessTokenDao) {
    this.applicationDao = applicationDao;
    this.clientSecretDao = clientSecretDao;
    this.componentDao = componentDao;
    this.routespaceDao = routespaceDao;
    this.applicationConverter = applicationConverter;
    this.applicationRouteDao = applicationRouteDao;
    this.accessTokenDao = accessTokenDao;
  }

  public List<Application> findAll(List<PropertyFilter> filter) {
    return applicationDao.findAll(filter);
  }

  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    Optional<Application> optional =
        this.applicationDao.findOne(
            PropertyFilter.builder().equal("clientId", clientId).equal("enabled", true).build());
    if (!optional.isPresent()) {
      throw new ClientRegistrationException("[client_id=" + clientId + "]不存在");
    }
    return optional.get();
  }

  @Transactional
  public Optional<Application> findByClientIdWithRoute(String id, String space) {
    return this.applicationDao.findByClientIdWithRoute(id);
  }

  @Transactional
  public Optional<Application> findByIdWithRoute(Long id, String space) {
    return this.applicationDao.findByIdWithRoute(id);
  }

  @Transactional
  public List<ApplicationRoute> findRouteAllByApplicationAndSpaceWithComponent(
      Long applicationId, String space) {
    return this.applicationRouteDao.findAllByApplicationAndSpaceWithComponent(applicationId, space);
  }

  @Transactional
  public Application createApplication(OAuthApplication app) {
    String clientId = StringUtil.generateNonceString(NONCE_CHARS, 20);
    String clientSecret = StringUtil.generateNonceString(NONCE_CHARS, 40);
    List<ClientSecret> clientSecrets = new ArrayList<>();
    clientSecrets.add(ClientSecret.builder().id(1L).client(clientId).secret(clientSecret).build());
    return Application.builder().clientId(clientId).clientSecretsAlias(clientSecrets).build();
  }

  @Transactional
  public Application createApplication(NativeApplication nativeApplication) {
    List<Routespace> routespaces =
        this.routespaceDao.findByIdsWithApplicationTemplateRoute(
            nativeApplication.getRoutespaces());

    String clientId = StringUtil.generateNonceString(NONCE_CHARS, 20);
    String clientSecretStr = StringUtil.generateNonceString(NONCE_CHARS, 40);

    // 创建密钥
    List<ClientSecret> clientSecrets = new ArrayList<>();
    ClientSecret clientSecret =
        clientSecretDao.save(
            ClientSecret.builder().client(clientId).secret(clientSecretStr).build());
    clientSecrets.add(clientSecret);

    // 创建应用
    Application application =
        Application.builder()
            .type(ApplicationType.NATIVE)
            .name(nativeApplication.getName())
            .description(nativeApplication.getDescription())
            .clientId(clientId)
            .clientSecretsAlias(clientSecrets)
            .routespaces(routespaces)
            .routes(new ArrayList<>())
            .build();

    // 生成应用路由
    List<ApplicationRoute> routes = new ArrayList<>();
    for (Routespace routespace : routespaces) {
      List<ApplicationTemplateRoute> templateRoutes =
          ObjectUtil.tree(
              routespace.getApplicationTemplate().getRoutes(),
              "id",
              "parent.id",
              "routes",
              item -> {
                ApplicationTemplateRoute route =
                    ObjectUtil.clone(item, "routes", "application", "component");
                Component component = item.getComponent();
                if (component == null) {
                  return route;
                }
                if (component.getScope() == ComponentScope.ROUTE) {
                  route.setComponent(this.componentDao.save(ObjectUtil.clone(component, "id")));
                } else {
                  route.setComponent(Component.builder().id(component.getId()).build());
                }
                return route;
              });
      List<ApplicationRoute> spaceRoutes =
          applicationConverter.toRoutes(templateRoutes, routespace);
      routes.addAll(ObjectUtil.flat(spaceRoutes, "routes"));
    }
    application.setRoutes(routes);

    // 保存应用
    this.applicationDao.save(application);
    return application;
  }

  @Transactional
  public void deleteApplication(Long id) {
    List<ApplicationRoute> routes =
        this.applicationRouteDao.findAll(
            PropertyFilter.builder()
                .equal("component.scope", ComponentScope.ROUTE)
                .equal("application.id", id)
                .isNotNull("component")
                .build());
    List<Component> components =
        routes.stream().map(item -> item.getComponent()).collect(Collectors.toList());

    this.applicationDao.deleteById(id);

    // 删除对应的路由组件
    this.componentDao.deleteAll(components);
  }
}
