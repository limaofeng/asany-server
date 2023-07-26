package cn.asany.security.core.service;

import cn.asany.security.core.dao.ResourceTypeDao;
import cn.asany.security.core.domain.ResourceType;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * 资源类型
 *
 * @author limaofeng
 */
@Service
public class ResourceTypeService {

  private final ResourceTypeDao resourceTypeDao;
  private final Cache cache;

  public static final String CACHE_KEY = "PERMISSION";

  public ResourceTypeService(CacheManager cacheManager, ResourceTypeDao resourceTypeDao) {
    this.resourceTypeDao = resourceTypeDao;
    this.cache = cacheManager.getCache(CACHE_KEY);
  }

  public Optional<ResourceType> findByArn(String arn) {
    return Optional.ofNullable(
        cache.get(
            ResourceTypeService.class.getName() + ".findByArn#" + arn,
            () ->
                this.resourceTypeDao
                    .findOne(PropertyFilter.newFilter().equal("arn", arn))
                    .map(
                        item -> {
                          Hibernate.unproxy(item);
                          return item;
                        })
                    .orElse(null)));
  }

  public ResourceType save(ResourceType resourceType) {
    try {
      return this.resourceTypeDao.save(resourceType);
    } finally {
      cache.evict(ResourceTypeService.class.getName() + ".findByArn#" + resourceType.getArn());
    }
  }
}
