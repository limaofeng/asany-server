package cn.asany.nuwa.app;

import cn.asany.cms.module.CmsModuleProperties;
import cn.asany.nuwa.YamlUtils;
import cn.asany.nuwa.app.domain.Application;
import cn.asany.nuwa.app.domain.ApplicationDependency;
import cn.asany.nuwa.app.domain.ApplicationMenu;
import cn.asany.nuwa.app.domain.ApplicationRoute;
import cn.asany.nuwa.app.service.ApplicationMenuService;
import cn.asany.nuwa.app.service.ApplicationRouteService;
import cn.asany.nuwa.app.service.ApplicationService;
import cn.asany.nuwa.app.service.dto.NativeApplication;
import cn.asany.shanhai.TestApplication;
import cn.asany.ui.library.service.LibraryService;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.oauth2.core.ClientSecretType;
import org.jfantasy.framework.util.common.StringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationServiceTest {

  @Autowired private ApplicationService applicationService;
  @Autowired private ApplicationRouteService applicationRouteService;
  @Autowired private ApplicationMenuService applicationMenuService;
  @Autowired private LibraryService libraryService;

  @Test
  void createApplicationFromYaml() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("app.yml");
    // 调基础工具类的方法
    NativeApplication app = YamlUtils.load(inputStream);
    assert app.getName().equals("website");
    applicationService.deleteApplication(app.getClientId());
    Application application = applicationService.createApplication(app);
    log.debug(
        String.format(
            "应用 %s 已经创建成功，ClientId = %s ClientSecret = %s",
            application.getName(),
            application.getClientId(),
            application.getClientSecret(ClientSecretType.OAUTH)));
  }

  @Test
  void createAdminApplicationFromYaml() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("app_admin.yml");
    // 调基础工具类的方法
    YamlUtils.addModuleClass("cms", CmsModuleProperties.class);
    NativeApplication app = YamlUtils.load(inputStream);
    assert app.getName().equals("admin");
    applicationService.deleteApplication(app.getClientId());
    Application application = applicationService.createApplication(app);
    log.debug(
        String.format(
            "应用 %s 已经创建成功，ClientId = %s ClientSecret = %s",
            application.getName(),
            application.getClientId(),
            application.getClientSecret(ClientSecretType.OAUTH)));
  }

  @Test
  void createWxbAdminApplicationFromYaml() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("app_admin_wxb.yml");
    // 调基础工具类的方法
    YamlUtils.addModuleClass("cms", CmsModuleProperties.class);
    NativeApplication app = YamlUtils.load(inputStream);
    assert app.getName().equals("admin_wxb");
    applicationService.deleteApplication(app.getClientId());
    Application application = applicationService.createApplication(app);
    log.debug(
        String.format(
            "应用 %s 已经创建成功，ClientId = %s ClientSecret = %s",
            application.getName(),
            application.getClientId(),
            application.getClientSecret(ClientSecretType.OAUTH)));
  }

  @Test
  void createMobileApplicationFromYaml() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("app_mobile.yml");
    // 调基础工具类的方法
    NativeApplication app = YamlUtils.load(inputStream);
    assert app.getName().equals("mobile");
    applicationService.deleteApplication(app.getClientId());
    Application application = applicationService.createApplication(app);
    log.debug(
        String.format(
            "应用 %s 已经创建成功，ClientId = %s ClientSecret = %s",
            application.getName(),
            application.getClientId(),
            application.getClientSecret(ClientSecretType.OAUTH)));
  }

  @Test
  void establishComponentAssociation() {
    Long appId = 355L;

    Application app = this.applicationService.findDetailsById(appId, true, true).orElse(null);

    assert app != null;

    String libraryId =
        this.applicationService.findDependencies(app.getId()).stream()
            .filter(item -> item.getName().equals("component.library"))
            .findFirst()
            .map(ApplicationDependency::getValue)
            .orElse(null);

    List<ApplicationRoute> routes =
        this.applicationRouteService.findAll(
            PropertyFilter.newFilter().equal("application.id", appId));

    List<ApplicationMenu> menus =
        this.applicationMenuService.findAll(
            PropertyFilter.newFilter().equal("application.id", appId));

    assert libraryId != null;

    for (ApplicationRoute route : routes) {

      if (route.getComponent() != null) {
        String tag = fullName("页面", route);
        if (StringUtil.isNotBlank(tag)) {
          this.libraryService.addComponent(Long.valueOf(libraryId), route.getComponent(), tag);
        } else {
          this.libraryService.addComponent(Long.valueOf(libraryId), route.getComponent());
        }
      }

      if (route.getBreadcrumb() != null) {
        String tag = fullName("其他组件", route);
        if (StringUtil.isNotBlank(tag)) {
          this.libraryService.addComponent(Long.valueOf(libraryId), route.getBreadcrumb(), tag);
        } else {
          this.libraryService.addComponent(Long.valueOf(libraryId), route.getBreadcrumb());
        }
      }
    }

    for (ApplicationMenu menu : menus) {
      if (menu.getComponent() != null) {
        String tag = "其他组件/菜单";
        if (StringUtil.isNotBlank(tag)) {
          this.libraryService.addComponent(Long.valueOf(libraryId), menu.getComponent(), tag);
        } else {
          this.libraryService.addComponent(Long.valueOf(libraryId), menu.getComponent());
        }
      }
    }
  }

  public String fullName(String prefix, ApplicationRoute route) {
    List<String> names = new ArrayList<>();

    route = route.getParent();

    while (route != null) {
      if (StringUtil.isNotBlank(route.getName())) {
        names.add(route.getName());
      }
      route = route.getParent();
    }

    Collections.reverse(names);

    if (!names.isEmpty()) {
      names.add(0, prefix);
    }

    return StringUtil.join(names.toArray(new String[0]), "/");
  }
}
