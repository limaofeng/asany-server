package cn.asany.cms.article.service;

import cn.asany.cms.article.bean.Banner;
import cn.asany.cms.article.bean.enums.BackgroundType;
import cn.asany.cms.article.dao.BannerDao;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
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
  public List<Banner> findAll(List<PropertyFilter> filters, OrderBy orderBy) {
    return bannerDao.findAll(filters, orderBy != null ? orderBy.toSort() : Sort.unsorted());
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
