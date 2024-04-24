package cn.asany.crm.core.dao;

import cn.asany.crm.core.domain.Customer;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("crm.customerDao")
public interface CustomerDao extends AnyJpaRepository<Customer, Long> {}
