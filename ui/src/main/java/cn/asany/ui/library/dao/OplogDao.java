package cn.asany.ui.library.dao;

import cn.asany.ui.library.bean.Oplog;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OplogDao extends JpaRepository<Oplog, Long> {}
