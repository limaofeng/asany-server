package cn.asany.security.core.service;

import cn.asany.security.core.dao.ResourceActionDao;
import cn.asany.security.core.domain.ResourceAction;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * 操作
 *
 * @author limaofeng
 */
@Service
public class ResourceActionService {

  private final ResourceActionDao resourceActionDao;
  private final Cache cache;

  public static final String CACHE_KEY = "PERMISSION";

  public ResourceActionService(CacheManager cacheManager, ResourceActionDao resourceActionDao) {
    this.cache = cacheManager.getCache(CACHE_KEY);
    this.resourceActionDao = resourceActionDao;
  }

  public Optional<ResourceAction> findById(String id) {
    ResourceAction action =
        cache.get(
            ResourceActionService.class.getName() + ".findById#" + id,
            () ->
                this.resourceActionDao
                    .findById(id)
                    .map(
                        item -> {
                          Hibernate.unproxy(item);
                          item.getResourceTypes().forEach(Hibernate::unproxy);
                          return item;
                        })
                    .orElse(null));
    return Optional.ofNullable(action);
  }

  public ResourceAction save(ResourceAction action) {
    try {
      return this.resourceActionDao.save(action);
    } finally {
      cache.evict(ResourceActionService.class.getName() + ".findById#" + action.getId());
    }
  }
}
