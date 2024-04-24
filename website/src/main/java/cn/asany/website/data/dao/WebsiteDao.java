package cn.asany.website.data.dao;

import cn.asany.website.data.domain.Website;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteDao extends AnyJpaRepository<Website, Long> {}
