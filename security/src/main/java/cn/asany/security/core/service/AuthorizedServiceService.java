package cn.asany.security.core.service;

import cn.asany.security.core.dao.AuthorizedServiceDao;
import cn.asany.security.core.domain.AuthorizedService;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * 可授权的服务
 *
 * @author limaofeng
 */
@Service
public class AuthorizedServiceService {

  private final AuthorizedServiceDao authorizedServiceDao;

  public AuthorizedServiceService(AuthorizedServiceDao authorizedServiceDao) {
    this.authorizedServiceDao = authorizedServiceDao;
  }

  public AuthorizedService save(AuthorizedService service) {
    return this.authorizedServiceDao.save(service);
  }

  public Optional<AuthorizedService> findById(String id) {
    return this.authorizedServiceDao.findById(id);
  }
}
