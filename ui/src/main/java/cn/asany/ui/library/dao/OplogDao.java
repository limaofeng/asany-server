package cn.asany.ui.library.dao;

import cn.asany.ui.library.domain.Oplog;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OplogDao extends AnyJpaRepository<Oplog, Long> {}
