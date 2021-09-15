package cn.asany.cms.article.dao;

import cn.asany.cms.article.bean.Banner;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerDao extends JpaRepository<Banner, Long> {}
