package cn.asany.storage.data.dao;

import cn.asany.storage.data.domain.Space;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceDao extends AnyJpaRepository<Space, String> {}
