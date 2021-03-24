package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.bean.ModelRelation;
import cn.asany.shanhai.core.bean.NameServer;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameServerDao extends JpaRepository<NameServer, Long> {
}
