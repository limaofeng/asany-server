package cn.asany.cms.article.dao;

import cn.asany.cms.article.bean.Banner;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 横幅 存储库
 *
 * @author limaofeng
 */
@Repository
public interface BannerDao extends JpaRepository<Banner, Long> {}
