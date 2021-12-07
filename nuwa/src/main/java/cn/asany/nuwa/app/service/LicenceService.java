package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.bean.Licence;
import cn.asany.nuwa.app.bean.enums.LicenceStatus;
import cn.asany.nuwa.app.dao.LicenceDao;
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
        PropertyFilter.builder()
            .equal("application.id", app)
            .equal("owner.id", org)
            .equal("status", LicenceStatus.ACTIVE)
            .build());
  }
}
