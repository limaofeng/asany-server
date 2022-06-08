package cn.asany.website.data.dao;

import cn.asany.website.data.domain.Website;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteDao extends JpaRepository<Website, Long> {}
