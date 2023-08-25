package cn.asany.security.core.service;

import cn.asany.security.core.dao.ResourceTypeDao;
import cn.asany.security.core.domain.AuthorizedService;
import cn.asany.security.core.domain.ResourceType;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资源类型
 *
 * @author limaofeng
 */
@Service
public class ResourceTypeService {

  private final ResourceTypeDao resourceTypeDao;
  private final Cache cache;

  private final AuthorizedServiceService authorizedServiceService;

  public static final String CACHE_KEY = "PERMISSION";

  public ResourceTypeService(
      AuthorizedServiceService authorizedServiceService,
      CacheManager cacheManager,
      ResourceTypeDao resourceTypeDao) {
    this.resourceTypeDao = resourceTypeDao;
    this.cache = cacheManager.getCache(CACHE_KEY);
    this.authorizedServiceService = authorizedServiceService;
  }

  @Cacheable(key = "targetClass + methodName + '#' + #p0.toString()", value = CACHE_KEY)
  @Transactional(readOnly = true)
  public Optional<ResourceType> findByResourceName(String resourceName) {
    return this.resourceTypeDao.findOne(
        PropertyFilter.newFilter().equal("resourceName", resourceName));
  }

  @Cacheable(key = "targetClass + methodName + '#' + #p0.toString()", value = CACHE_KEY)
  @Transactional(readOnly = true)
  public Optional<ResourceType> findByLabel(String label) {
    return this.resourceTypeDao
        .findOne(PropertyFilter.newFilter().equal("label", label))
        .map(
            item -> {
              Hibernate.unproxy(item);
              Hibernate.unproxy(item.getService());
              item.getArns().forEach(Hibernate::unproxy);
              return item;
            });
  }

  @Transactional(readOnly = true)
  public Optional<ResourceType> findByArn(String arn) {
    return Optional.ofNullable(
        cache.get(
            ResourceTypeService.class.getName() + ".findByArn#" + arn,
            () ->
                this.resourceTypeDao
                    .findOne(PropertyFilter.newFilter().equal("arns", arn))
                    .map(
                        item -> {
                          Hibernate.unproxy(item);
                          return item;
                        })
                    .orElse(null)));
  }

  @CacheEvict(key = "targetClass + 'findByLabel#' + #p0.label.toString()", value = CACHE_KEY)
  @Transactional(rollbackFor = Exception.class)
  public ResourceType save(ResourceType resourceType) {
    return this.resourceTypeDao.save(resourceType);
  }

  @CacheEvict(key = "targetClass + 'findByLabel#' + #p0.label.toString()", value = CACHE_KEY)
  @Transactional(rollbackFor = Exception.class)
  public ResourceType update(ResourceType resourceType) {
    return this.resourceTypeDao.update(resourceType);
  }

  @CacheEvict(key = "targetClass + 'findByLabel#' + #p0.label.toString()", value = CACHE_KEY)
  @Transactional(rollbackFor = Exception.class)
  public void saveOrUpdate(ResourceType resourceType) {
    Optional<ResourceType> optional =
        this.resourceTypeDao.findOne(
            PropertyFilter.newFilter().equal("label", resourceType.getLabel()));
    if (optional.isPresent()) {
      ResourceType sourceResourceType = optional.get();
      sourceResourceType.setResourceName(resourceType.getResourceName());
      sourceResourceType.getArns().addAll(resourceType.getArns());
      sourceResourceType.setName(resourceType.getName());
      sourceResourceType.setDescription(resourceType.getDescription());
      this.resourceTypeDao.update(sourceResourceType);
    } else {
      String[] values = resourceType.getLabel().split(":");
      resourceType.setService(getServiceOrSave(values[0]));
      this.resourceTypeDao.save(resourceType);
    }
  }

  private AuthorizedService getServiceOrSave(String id) {
    return this.authorizedServiceService
        .findById(id)
        .orElseGet(
            () -> this.authorizedServiceService.save(AuthorizedService.builder().id(id).build()));
  }
}
