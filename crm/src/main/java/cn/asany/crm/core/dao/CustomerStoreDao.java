package cn.asany.crm.core.dao;

import cn.asany.crm.core.domain.CustomerStore;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerStoreDao extends JpaRepository<CustomerStore, Long> {}
