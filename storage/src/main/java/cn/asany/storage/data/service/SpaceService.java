package cn.asany.storage.data.service;

import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.dao.SpaceDao;
import java.util.List;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
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

  public Space save(Space directory) {
    spaceDao.save(directory);
    return directory;
  }

  public Space get(String id) {
    return spaceDao.getById(id);
  }

  public boolean direcroryKeyUnique(String key) {
    return false; // this.directoryDao.findUniqueBy("key", key) == null;
  }
}
