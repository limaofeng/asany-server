package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.Module;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleDao extends JpaRepository<Module, Long> {}
