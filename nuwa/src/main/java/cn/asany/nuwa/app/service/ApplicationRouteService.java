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
package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.dao.ApplicationRouteDao;
import cn.asany.nuwa.app.domain.ApplicationRoute;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.SortNodeLoader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ApplicationRouteService {

  private final ApplicationRouteDao routeDao;
  private final ApplicationService applicationService;

  public ApplicationRouteService(
      ApplicationRouteDao applicationRouteDao, ApplicationService applicationService) {
    this.routeDao = applicationRouteDao;
    this.applicationService = applicationService;
  }

  public Optional<ApplicationRoute> getRoute(Long id) {
    return this.routeDao.findById(id);
  }

  public ApplicationRoute create(ApplicationRoute route) {
    ObjectUtil.resort(route, sortNodeLoader, this.routeDao::update);

    if (route.getEnabled() == null) {
      route.setEnabled(Boolean.FALSE);
    }

    if (route.getAuthorized() == null) {
      route.setAuthorized(Boolean.FALSE);
    }

    if (route.getHideInBreadcrumb() == null) {
      route.setHideInBreadcrumb(Boolean.FALSE);
    }

    if (route.getParentId() != null) {
      route.setParent(this.routeDao.getReferenceById((Long) route.getParentId()));
    }

    applicationService.clearAppCache(route.getApplication().getId());

    return this.routeDao.save(route);
  }

  public ApplicationRoute update(Long id, ApplicationRoute route, Boolean merge) {
    route.setId(id);

    ApplicationRoute old = this.routeDao.getReferenceById(id);
    route.setApplication(old.getApplication());

    ObjectUtil.resort(route, sortNodeLoader, this.routeDao::update);

    applicationService.clearAppCache(old.getApplication().getId());

    return this.routeDao.update(route, merge);
  }

  public void delete(Long id) {
    Optional<ApplicationRoute> optional = this.routeDao.findById(id);
    if (!optional.isPresent()) {
      return;
    }
    this.routeDao.deleteById(id);

    ApplicationRoute route = optional.get();
    ObjectUtil.resort(route, sortNodeLoader, this.routeDao::update);

    applicationService.clearAppCache(route.getApplication().getId());
  }

  private final SortNodeLoader<ApplicationRoute> sortNodeLoader = new RouteSortNodeLoader();

  public List<ApplicationRoute> findAll(PropertyFilter filter) {
    return this.routeDao.findAll(filter);
  }

  private class RouteSortNodeLoader implements SortNodeLoader<ApplicationRoute> {
    @Override
    public List<ApplicationRoute> getAll(Serializable parentId, ApplicationRoute route) {
      PropertyFilter filter = PropertyFilter.newFilter();
      if (route.getParent() == null) {
        filter.isNull("parent");
        filter.equal("application.id", route.getApplication().getId());
      } else {
        filter.equal("parent.id", parentId);
      }
      return ApplicationRouteService.this.routeDao.findAll(filter, Sort.by("index").ascending());
    }

    @Override
    public ApplicationRoute load(Serializable id) {
      if (id == null) {
        return null;
      }
      return ApplicationRouteService.this.routeDao.findById((Long) id).orElse(null);
    }
  }
}
