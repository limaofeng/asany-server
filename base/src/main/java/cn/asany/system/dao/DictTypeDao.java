package cn.asany.system.dao;

import cn.asany.system.domain.DictType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictTypeDao extends JpaRepository<DictType, String> {}
