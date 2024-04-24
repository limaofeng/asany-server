package cn.asany.system.dao;

import cn.asany.system.domain.Dict;
import cn.asany.system.domain.DictKey;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface DictDao extends AnyJpaRepository<Dict, DictKey> {

  @Modifying
  int deleteDictByType(String type);
}
