package cn.asany.nuwa.app.service;

import cn.asany.base.IModuleLoader;
import cn.asany.base.IModuleProperties;
import cn.asany.nuwa.app.converter.ApplicationConverter;
import cn.asany.nuwa.app.dao.*;
import cn.asany.nuwa.app.domain.*;
import cn.asany.nuwa.app.domain.enums.ApplicationType;
import cn.asany.nuwa.app.domain.enums.MenuType;
import cn.asany.nuwa.app.domain.enums.RouteType;
import cn.asany.nuwa.app.service.dto.NativeApplication;
import cn.asany.nuwa.app.service.dto.NuwaMenu;
import cn.asany.nuwa.app.service.dto.NuwaRoute;
import cn.asany.nuwa.app.service.dto.OAuthApplication;
import cn.asany.nuwa.template.dao.ApplicationTemplateDao;
import cn.asany.nuwa.template.domain.ApplicationTemplate;
import cn.asany.nuwa.template.domain.ApplicationTemplateMenu;
import cn.asany.nuwa.template.domain.ApplicationTemplateRoute;
import cn.asany.ui.resources.dao.ComponentDao;
import cn.asany.ui.resources.domain.Component;
import cn.asany.ui.resources.domain.enums.ComponentScope;
import java.util.*;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.security.oauth2.core.ClientDetails;
import org.jfantasy.framework.security.oauth2.core.ClientDetailsService;
import org.jfantasy.framework.security.oauth2.core.ClientRegistrationException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 应用服务
 *
 * @author limaofeng
 */
@Service
public class ApplicationService implements ClientDetailsService {

  public static final String NONCE_CHARS = "abcdef0123456789";
  public static final String CACHE_KEY = "NUWA";

  private final ApplicationDao applicationDao;
  private final ApplicationDependencyDao applicationDependencyDao;
  private final ClientSecretDao clientSecretDao;
  private final ApplicationRouteDao applicationRouteDao;
  private final ApplicationMenuDao applicationMenuDao;
  private final ApplicationTemplateDao applicationTemplateDao;
  private final ComponentDao componentDao;
  private final RoutespaceDao routespaceDao;
  private final ApplicationConverter applicationConverter;
  private final IModuleLoader moduleLoader;
  private final CacheManager cacheManager;

  public ApplicationService(
      CacheManager cacheManager,
      ApplicationDao applicationDao,
      ApplicationDependencyDao applicationDependencyDao,
      ClientSecretDao clientSecretDao,
      ComponentDao componentDao,
      RoutespaceDao routespaceDao,
      ApplicationConverter applicationConverter,
      ApplicationRouteDao applicationRouteDao,
      ApplicationMenuDao applicationMenuDao,
      ApplicationTemplateDao applicationTemplateDao,
      IModuleLoader moduleLoader) {
    this.applicationDao = applicationDao;
    this.clientSecretDao = clientSecretDao;
    this.componentDao = componentDao;
    this.routespaceDao = routespaceDao;
    this.applicationConverter = applicationConverter;
    this.applicationRouteDao = applicationRouteDao;
    this.applicationMenuDao = applicationMenuDao;
    this.applicationTemplateDao = applicationTemplateDao;
    this.applicationDependencyDao = applicationDependencyDao;
    this.moduleLoader = moduleLoader;
    this.cacheManager = cacheManager;
  }

  public List<Application> findAll(PropertyFilter filter) {
    return applicationDao.findAll(filter);
  }

  @Override
  @Cacheable(key = "targetClass + '.' +  methodName + '#' + #p0", value = CACHE_KEY)
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    Optional<Application> optional =
        this.applicationDao.findOneWithClientDetails(
            PropertyFilter.newFilter().equal("clientId", clientId).equal("enabled", true));
    if (!optional.isPresent()) {
      throw new ClientRegistrationException("[client_id=" + clientId + "]不存在");
    }
    return optional.get();
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public boolean existsByClientId(String clientId) {
    return this.applicationDao.exists(PropertyFilter.newFilter().equal("clientId", clientId));
  }

  @Transactional(rollbackFor = RuntimeException.class)
  @Cacheable(
      key = "targetClass  + '.' +  methodName + '#' + #p0",
      value = CACHE_KEY,
      condition = "#p1 && #p2")
  public Optional<Application> findDetailsByClientId(
      String id, boolean hasFetchRoutes, boolean hasFetchMenus) {
    if (hasFetchRoutes && hasFetchMenus) {
      return this.applicationDao.findDetailsByClientId(id);
    }
    if (hasFetchRoutes) {
      return this.applicationDao.findOneWithRoutesByClientId(id);
    }
    if (hasFetchMenus) {
      return this.applicationDao.findOneWithMenusByClientId(id);
    }
    return this.applicationDao.findOneBy("clientId", id);
  }

  @Transactional(readOnly = true)
  @Cacheable(key = "targetClass  + '.' +  methodName + '#' + #p0", value = CACHE_KEY)
  public List<ApplicationDependency> findDependencies(Long id) {
    return this.applicationDependencyDao.findAll(
        PropertyFilter.newFilter().equal("application.id", id), Sort.by("createdAt").ascending());
  }

  @Transactional(rollbackFor = RuntimeException.class)
  @Cacheable(
      key = "targetClass + '.' + methodName + '#' + #p0",
      value = CACHE_KEY,
      condition = "#p1 && #p2")
  public Optional<Application> findDetailsById(
      Long id, boolean hasFetchRoutes, boolean hasFetchMenus) {
    if (hasFetchRoutes && hasFetchMenus) {
      return this.applicationDao.findDetailsById(id);
    }
    if (hasFetchRoutes) {
      return this.applicationDao.findOneWithRoutesById(id);
    }
    if (hasFetchMenus) {
      return this.applicationDao.findOneWithMenusById(id);
    }
    return this.applicationDao.findById(id);
  }

  @Transactional
  public List<ApplicationRoute> findRouteAllByApplicationAndSpaceWithComponent(
      Long applicationId, String space) {
    return this.applicationRouteDao.findAllByApplicationAndSpaceWithComponent(applicationId, space);
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public Application createApplication(OAuthApplication app) {
    Application application = this.applicationConverter.oauthAppToApp(app);
    String clientId = StringUtil.generateNonceString(NONCE_CHARS, 20);
    String clientSecret = StringUtil.generateNonceString(NONCE_CHARS, 40);
    List<ClientSecret> clientSecrets = new ArrayList<>();
    clientSecrets.add(ClientSecret.builder().id(1L).client(clientId).secret(clientSecret).build());
    application.setClientId(clientId);
    application.setClientSecretsAlias(clientSecrets);
    return application;
  }

  @Transactional
  public Application createApplication(NativeApplication nativeApplication) {
    return createApplication(nativeApplication, Optional.empty());
  }

  @Transactional
  public Application createApplication(NativeApplication nativeApplication, Long template) {
    Optional<ApplicationTemplate> optionalTemplate = this.applicationTemplateDao.findById(template);
    if (!optionalTemplate.isPresent()) {
      throw new ValidationException("应用模版" + template + "不存在");
    }
    return createApplication(nativeApplication, optionalTemplate);
  }

  public Application createApplication(
      NativeApplication nativeApplication, Optional<ApplicationTemplate> template) {

    if (StringUtil.isBlank(nativeApplication.getRoutespace())) {
      nativeApplication.setRoutespace(Routespace.DEFAULT_ROUTESPACE_WEB.getId());
    }

    Optional<Routespace> optionalRoutespace =
        this.routespaceDao.findByIdWithApplicationTemplate(nativeApplication.getRoutespace());

    if (!optionalRoutespace.isPresent()) {
      throw new ValidationException("应用路由 Space " + template + "不存在");
    }

    Routespace routespace = optionalRoutespace.get();

    String clientId =
        ObjectUtil.defaultValue(
            nativeApplication.getClientId(), () -> StringUtil.generateNonceString(NONCE_CHARS, 20));
    String clientSecretStr =
        ObjectUtil.defaultValue(
            nativeApplication.getClientSecret(),
            () -> StringUtil.generateNonceString(NONCE_CHARS, 40));

    // 创建密钥
    List<ClientSecret> clientSecrets = new ArrayList<>();
    ClientSecret clientSecret =
        clientSecretDao.save(
            ClientSecret.builder().client(clientId).secret(clientSecretStr).build());
    clientSecrets.add(clientSecret);

    // 创建应用
    Application application =
        Application.builder()
            .type(ObjectUtil.defaultValue(nativeApplication.getType(), ApplicationType.Native))
            .name(nativeApplication.getName())
            .description(nativeApplication.getDescription())
            .clientId(clientId)
            .clientSecretsAlias(clientSecrets)
            .routespaces(Arrays.stream(new Routespace[] {routespace}).collect(Collectors.toList()))
            .routes(new HashSet<>())
            .build();

    // 生成应用路由
    ApplicationTemplate applicationTemplate = template.orElse(routespace.getApplicationTemplate());
    if (nativeApplication.getRoutes() != null && !nativeApplication.getRoutes().isEmpty()) {
      application.setRoutes(getRoutesFromNuwa(nativeApplication.getRoutes(), routespace));
    } else if (applicationTemplate != null) {
      application.setRoutes(getRoutesFromTemplate(applicationTemplate, routespace));
    }

    // 生成菜单
    if (nativeApplication.getMenus() != null && !nativeApplication.getMenus().isEmpty()) {
      application.setMenus(getMenusFromNuwa(nativeApplication.getMenus()));
    } else if (applicationTemplate != null) {
      application.setMenus(getMenusFromTemplate(applicationTemplate));
    }

    // 保存应用
    this.applicationDao.save(application);

    List<IModuleProperties> modules =
        ObjectUtil.defaultValue(nativeApplication.getModules(), Collections.emptyList());
    for (IModuleProperties module : modules) {
      this.moduleLoader.load(module.getType(), module);
    }

    return application;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteApplication(String clientId) {
    Optional<Application> application =
        this.applicationDao.findOne(PropertyFilter.newFilter().equal("clientId", clientId));
    if (!application.isPresent()) {
      return;
    }
    deleteApplication(application.get().getId());
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteApplication(Long id) {
    Application app = this.applicationDao.getReferenceById(id);
    // 路由组件
    List<ApplicationRoute> routes =
        this.applicationRouteDao.findAll(
            PropertyFilter.newFilter()
                .equal("component.scope", ComponentScope.ROUTE)
                .equal("application.id", id)
                .isNotNull("component"));
    List<Component> components =
        routes.stream().map(ApplicationRoute::getComponent).collect(Collectors.toList());

    // 菜单组件
    List<ApplicationMenu> menus =
        this.applicationMenuDao.findAll(
            PropertyFilter.newFilter()
                .equal("component.scope", ComponentScope.MENU)
                .equal("application.id", id)
                .isNotNull("component"));
    components.addAll(
        menus.stream().map(ApplicationMenu::getComponent).collect(Collectors.toList()));

    String clientId = app.getClientId();

    // 删除应用
    this.applicationDao.deleteById(id);

    // 删除对应的组件
    this.componentDao.deleteAll(components);

    Cache cache = this.cacheManager.getCache(CACHE_KEY);
    assert cache != null;
    cache.evictIfPresent(ApplicationService.class + ".loadClientByClientId#" + clientId);
    cache.evictIfPresent(ApplicationService.class + ".findDetailsByClientId#" + clientId);
    cache.evictIfPresent(ApplicationService.class + ".findDetailsById#" + id);
  }

  private Set<ApplicationMenu> getMenusFromNuwa(List<NuwaMenu> nuwaMenus) {
    List<Component> components = new ArrayList<>();
    List<ApplicationMenu> menus =
        ObjectUtil.recursive(
            nuwaMenus,
            (item, context) -> {
              ApplicationMenu menu = this.applicationConverter.toMenuFromNuwa(item);

              menu.setIndex(context.getIndex());
              menu.setLevel(context.getLevel());

              if (menu.getType() == null) {
                // 这里使用的是 item， 因为子项在之后才会执行
                if (item.getChildren() != null
                    && !item.getChildren().isEmpty()
                    && !menu.getHideChildrenInMenu()) {
                  menu.setType(MenuType.MENU);
                } else {
                  menu.setType(MenuType.URL);
                }
              }

              Component component = menu.getComponent();
              if (context.getLevel() != 1 && component != null) {
                menu.setComponent(component = null);
              }
              if (component == null) {
                return menu;
              }
              component.setScope(ComponentScope.MENU);
              components.add(component);

              return menu;
            });
    this.componentDao.saveAllInBatch(components);
    menus = ObjectUtil.flat(menus, "children");
    return new HashSet<>(menus);
  }

  private Set<ApplicationMenu> getMenusFromTemplate(ApplicationTemplate applicationTemplate) {
    List<ApplicationTemplateMenu> templateMenus =
        ObjectUtil.tree(applicationTemplate.getMenus(), "id", "parent.id", "children");
    List<ApplicationMenu> menus = applicationConverter.toMenusFromTemplate(templateMenus);
    return new HashSet<>(ObjectUtil.flat(menus, "children"));
  }

  private Set<ApplicationRoute> getRoutesFromNuwa(
      List<NuwaRoute> nuwaRoutes, Routespace routespace) {
    List<Component> components = new ArrayList<>();
    List<ApplicationRoute> routes =
        ObjectUtil.recursive(
            nuwaRoutes,
            (item, context) -> {
              ApplicationRoute route = this.applicationConverter.toRouteFromNuwa(item);
              route.setIndex(context.getIndex());
              route.setLevel(context.getLevel());
              route.setSpace(routespace);
              route.setType(RouteType.ROUTE);
              route.setEnabled(ObjectUtil.defaultValue(route.getEnabled(), true));
              route.setAuthorized(ObjectUtil.defaultValue(route.getAuthorized(), false));
              route.setLayout(
                  ObjectUtil.defaultValue(
                      route.getLayout(), () -> LayoutSettings.builder().build()));
              route.setHideInBreadcrumb(false);
              Component component = route.getComponent();
              if (component == null) {
                return route;
              }
              component.setScope(ComponentScope.ROUTE);
              components.add(component);
              return route;
            },
            "routes");
    this.componentDao.saveAllInBatch(components);
    return new HashSet<>(ObjectUtil.flat(routes, "routes"));
  }

  private Set<ApplicationRoute> getRoutesFromTemplate(
      ApplicationTemplate applicationTemplate, Routespace routespace) {
    List<ApplicationTemplateRoute> templateRoutes =
        ObjectUtil.tree(
            applicationTemplate.getRoutes(),
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
    List<ApplicationRoute> spaceRoutes = applicationConverter.toRoutes(templateRoutes, routespace);
    return new HashSet<>(ObjectUtil.flat(spaceRoutes, "routes"));
  }

  public Optional<ApplicationRoute> getRoute(Long id) {
    return this.applicationRouteDao.findById(id);
  }

  public void addRoute(ApplicationRoute route) {
    this.applicationRouteDao.save(route);
    Cache cache = this.cacheManager.getCache(CACHE_KEY);
    assert cache != null;
    Long appId = route.getApplication().getId();
    String clientId = route.getApplication().getClientId();
    cache.evictIfPresent(ApplicationService.class + ".loadClientByClientId#" + clientId);
    cache.evictIfPresent(ApplicationService.class + ".findDetailsByClientId#" + clientId);
    cache.evictIfPresent(ApplicationService.class + ".findDetailsById#" + appId);
  }

  public void clearAppCache(Long appId) {
    clearApplication(this.applicationDao.getReferenceById(appId));
  }

  public void clearApplication(Application application) {
    Cache cache = this.cacheManager.getCache(CACHE_KEY);
    assert cache != null;
    Long appId = application.getId();
    String clientId = application.getClientId();
    cache.evictIfPresent(ApplicationService.class + ".loadClientByClientId#" + clientId);
    cache.evictIfPresent(ApplicationService.class + ".findDetailsByClientId#" + clientId);
    cache.evictIfPresent(ApplicationService.class + ".findDetailsById#" + appId);
  }
}
