package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.bean.Service;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameServerDao extends JpaRepository<Service, Long> {
}
