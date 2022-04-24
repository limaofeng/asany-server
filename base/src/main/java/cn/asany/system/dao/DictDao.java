package cn.asany.system.dao;

import cn.asany.system.bean.Dict;
import cn.asany.system.bean.DictKey;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface DictDao extends JpaRepository<Dict, DictKey> {

  @Modifying
  int deleteDictByType(String type);
}
