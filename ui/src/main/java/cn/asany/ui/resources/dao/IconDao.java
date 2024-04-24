package cn.asany.ui.resources.dao;

import cn.asany.ui.resources.domain.Icon;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IconDao extends AnyJpaRepository<Icon, Long> {}
