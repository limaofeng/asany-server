package cn.asany.storage.data.service;

import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.dao.SpaceDao;
import java.util.List;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @author limaofeng */
@Service
@Transactional
public class SpaceService {

  private final SpaceDao spaceDao;

  public SpaceService(SpaceDao spaceDao) {
    this.spaceDao = spaceDao;
  }

  public Pager<Space> findPager(Pager<Space> pager, List<PropertyFilter> filters) {
    return spaceDao.findPager(pager, filters);
  }

  public void delete(String... ids) {
    for (String id : ids) {
      this.spaceDao.deleteById(id);
    }
  }

  @CachePut(key = "targetClass + '#' + #p0.id", cacheNames = "STORAGE")
  public Space save(Space space) {
    spaceDao.save(space);
    return space;
  }

  @Cacheable(key = "targetClass + '#' + #p0", cacheNames = "STORAGE")
  public Space get(String id) {
    Space space = this.spaceDao.getById(id);
    return (Space) Hibernate.unproxy(space);
  }

  public boolean direcroryKeyUnique(String key) {
    return false; // this.directoryDao.findUniqueBy("key", key) == null;
  }
}
