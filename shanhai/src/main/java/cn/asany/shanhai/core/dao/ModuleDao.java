package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.Module;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleDao extends AnyJpaRepository<Module, Long> {}
