package cn.asany.cms.article.service;

import cn.asany.cms.article.bean.Banner;
import cn.asany.cms.article.dao.BannerDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 横幅广告
 *
 * @author limaofeng
 */
@Service
public class BannerService {
  @Autowired private BannerDao bannerDao;

  public List<Banner> banners() {
    return bannerDao.findAll();
  }
}
