package cn.asany.system.dao;

import cn.asany.system.domain.DictType;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictTypeDao extends AnyJpaRepository<DictType, String> {}
