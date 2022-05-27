package cn.asany.system.dao;

import cn.asany.system.domain.Dict;
import cn.asany.system.domain.DictKey;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface DictDao extends JpaRepository<Dict, DictKey> {

  @Modifying
  int deleteDictByType(String type);
}
