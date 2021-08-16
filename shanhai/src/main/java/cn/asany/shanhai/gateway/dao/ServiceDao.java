package cn.asany.shanhai.gateway.dao;

import cn.asany.shanhai.gateway.bean.Service;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDao extends JpaRepository<Service, Long> {}
