/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.dao.LicenceDao;
import cn.asany.nuwa.app.domain.Licence;
import cn.asany.nuwa.app.domain.enums.LicenceStatus;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
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
            .equal("status", LicenceStatus.ACTIVE));
  }
}
