package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.dao.RoutespaceDao;
import cn.asany.nuwa.app.domain.Routespace;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RoutespaceService {

  private final RoutespaceDao routespaceDao;

  public RoutespaceService(RoutespaceDao routespaceDao) {
    this.routespaceDao = routespaceDao;
  }

  public List<Routespace> findAll() {
    return routespaceDao.findAll();
  }

  public Optional<Routespace> findById(String id) {
    return routespaceDao.findById(id);
  }

  @Transactional(rollbackOn = Exception.class)
  public Routespace createRoutespace(Routespace routespace) {
    return this.routespaceDao.save(routespace);
  }

  @Transactional(rollbackOn = Exception.class)
  public void deleteRoutespace(String id) {
    this.routespaceDao.deleteById(id);
  }
}
