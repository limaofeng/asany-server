/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.cms.article.listener;

import cn.asany.cms.article.dao.ArticleCategoryDao;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.domain.PageComponent;
import cn.asany.cms.article.event.ArticleCategoryPageUpdateEvent;
import cn.asany.nuwa.app.domain.Application;
import cn.asany.nuwa.app.domain.ApplicationRoute;
import cn.asany.nuwa.app.domain.LayoutSettings;
import cn.asany.nuwa.app.domain.enums.RouteType;
import cn.asany.nuwa.app.service.ApplicationService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PageUpdateListener implements ApplicationListener<ArticleCategoryPageUpdateEvent> {

  private final ApplicationService applicationService;
  private final ArticleCategoryDao articleCategoryDao;

  public PageUpdateListener(
      ApplicationService applicationService, ArticleCategoryDao articleCategoryDao) {
    this.applicationService = applicationService;
    this.articleCategoryDao = articleCategoryDao;
  }

  @Override
  public void onApplicationEvent(ArticleCategoryPageUpdateEvent event) {
    ArticleCategoryPageUpdateEvent.ArticleCategoryPageUpdateSource source =
        (ArticleCategoryPageUpdateEvent.ArticleCategoryPageUpdateSource) event.getSource();
    ArticleCategory category = source.getCategory();
    PageComponent page = source.getPage();
    ApplicationRoute route = page.getRoute();

    String[] ids = StringUtil.tokenizeToStringArray(category.getParent().getPath(), "/");

    Long id = Long.parseLong(ids[0]);

    Long appId = this.articleCategoryDao.getAppIdOfWebsite(id);

    Optional<Application> optionalApplication =
        applicationService.findDetailsById(appId, true, false);

    if (!optionalApplication.isPresent()) {
      return;
    }

    Application application = optionalApplication.get();

    Set<ApplicationRoute> routeList = application.getRoutes();

    Set<ApplicationRoute> routeTree =
        ObjectUtil.tree(routeList, "id", "parent.id", "routes", item -> item);

    ApplicationRoute layoutRoute = ObjectUtil.find(routeTree, "path", "/");
    List<ApplicationRoute> routes = layoutRoute.getRoutes();

    if (route.getId() == null) {
      routes = ObjectUtil.sort(routes, "index", "asc");
      routes.add(0, route);
      for (int i = 0; i < routes.size(); i++) {
        routes.get(i).setIndex(i);
      }
      route.setApplication(application);
      route.setEnabled(page.getEnabled());
      route.setComponent(page.getComponent());
      route.setLevel(2);
      route.setType(RouteType.ROUTE);
      route.setAuthorized(false);
      route.setParent(layoutRoute);
      route.setLayout(LayoutSettings.builder().pure(false).hideMenu(false).build());
      this.applicationService.addRoute(route);
    } else {
      this.applicationService.clearApplication(application);
    }

    log.debug("Application:" + optionalApplication.map(Application::getClientId));
  }
}
