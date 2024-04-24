package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.Banner;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 横幅 存储库
 *
 * @author limaofeng
 */
@Repository
public interface BannerDao extends AnyJpaRepository<Banner, Long> {}
