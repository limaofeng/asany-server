package cn.asany.crm.core.dao;

import cn.asany.crm.core.domain.CustomerStore;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerStoreDao extends AnyJpaRepository<CustomerStore, Long> {}
