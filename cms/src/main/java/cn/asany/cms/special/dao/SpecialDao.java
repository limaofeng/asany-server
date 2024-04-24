package cn.asany.cms.special.dao;

import cn.asany.cms.special.domain.Special;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialDao extends AnyJpaRepository<Special, String> {}
