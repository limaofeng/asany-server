package cn.asany.myjob.factory.service;

import cn.asany.myjob.factory.dao.ScreenDao;
import cn.asany.myjob.factory.domain.Screen;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScreenService {

  private final ScreenDao screenDao;

  public ScreenService(ScreenDao screenDao) {
    this.screenDao = screenDao;
  }

  public Page<Screen> findPage(Pageable pageable, PropertyFilter filter) {
    return this.screenDao.findPage(pageable, filter);
  }

  public List<Screen> findAll() {
    return this.screenDao.findAll();
  }

  public Screen delete(Long id) {
    Screen screen = this.screenDao.getReferenceById(id);
    this.screenDao.delete(screen);
    return screen;
  }

  @Transactional(rollbackFor = Exception.class)
  public Screen update(Long id, Screen screen, Boolean merge) {
    screen.setId(id);
    return this.screenDao.update(screen, merge);
  }

  public Screen save(Screen screen) {
    return this.screenDao.save(screen);
  }

  public Optional<Screen> findById(Long id) {
    return this.screenDao.findById(id);
  }

  public Optional<Screen> findScreenForIp(String ip) {
    return this.screenDao.findOne(PropertyFilter.newFilter().equal("boundIp", ip));
  }
}
