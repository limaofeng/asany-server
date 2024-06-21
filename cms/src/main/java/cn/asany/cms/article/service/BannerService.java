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
package cn.asany.cms.article.service;

import cn.asany.cms.article.dao.BannerDao;
import cn.asany.cms.article.domain.Banner;
import cn.asany.cms.article.domain.enums.BackgroundType;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 横幅广告
 *
 * @author limaofeng
 */
@Service
public class BannerService {
  private final BannerDao bannerDao;

  public BannerService(BannerDao bannerDao) {
    this.bannerDao = bannerDao;
  }

  @Transactional(readOnly = true)
  public List<Banner> findAll(PropertyFilter filter, Sort orderBy) {
    return bannerDao.findAll(filter, orderBy);
  }

  @Transactional(readOnly = true)
  public Optional<Banner> findById(Long id) {
    return this.bannerDao.findById(id);
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public Banner save(Banner banner) {
    if (banner.getBackgroundType() == null
        && banner.getBackground() != null
        && banner.getBackground().getMimeType() != null) {
      banner.setBackgroundType(BackgroundType.resolve(banner.getBackground().getMimeType()));
    }
    if (banner.getEnabled() == null) {
      banner.setEnabled(true);
    }
    return this.bannerDao.save(banner);
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public Banner update(Long id, Banner banner, boolean merge) {
    banner.setId(id);
    return this.bannerDao.update(banner, merge);
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public Boolean delete(Set<Long> ids) {
    this.bannerDao.deleteAllByIdInBatch(ids);
    return Boolean.TRUE;
  }
}
