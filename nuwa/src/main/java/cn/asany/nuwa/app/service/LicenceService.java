package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.dao.LicenceDao;
import cn.asany.nuwa.app.domain.Licence;
import cn.asany.nuwa.app.domain.enums.LicenceStatus;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 许可证服务
 *
 * @author limaofeng
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class LicenceService {

  private final LicenceDao licenceDao;

  public LicenceService(LicenceDao licenceDao) {
    this.licenceDao = licenceDao;
  }

  public Optional<Licence> findOneByActive(Long app, Long org) {
    return this.licenceDao.findOne(
        PropertyFilter.newFilter()
            .equal("application.id", app)
            .equal("ownership.id", org)
            .equal("status", LicenceStatus.ACTIVE)
            );
  }
}
