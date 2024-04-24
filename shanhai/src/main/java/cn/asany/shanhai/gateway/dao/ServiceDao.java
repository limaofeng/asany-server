package cn.asany.shanhai.gateway.dao;

import cn.asany.shanhai.gateway.domain.Service;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDao extends AnyJpaRepository<Service, Long> {}
