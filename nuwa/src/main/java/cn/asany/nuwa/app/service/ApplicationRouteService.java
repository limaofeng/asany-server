package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.dao.ApplicationRouteDao;
import cn.asany.nuwa.app.domain.ApplicationRoute;
import cn.asany.nuwa.app.domain.Routespace;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.SortNodeLoader;
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

    if (route.getSpace() == null) {
      route.setSpace(Routespace.DEFAULT_ROUTESPACE_WEB);
    }

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

  public List<ApplicationRoute> findAll(List<PropertyFilter> filters) {
    return this.routeDao.findAll(filters);
  }

  private class RouteSortNodeLoader implements SortNodeLoader<ApplicationRoute> {
    @Override
    public List<ApplicationRoute> getAll(Serializable parentId, ApplicationRoute route) {
      PropertyFilterBuilder builder = PropertyFilter.builder();
      if (route.getParent() == null) {
        builder.isNull("parent");
        builder.equal("application.id", route.getApplication().getId());
      } else {
        builder.equal("parent.id", parentId);
      }
      return ApplicationRouteService.this.routeDao.findAll(
          builder.build(), Sort.by("index").ascending());
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
