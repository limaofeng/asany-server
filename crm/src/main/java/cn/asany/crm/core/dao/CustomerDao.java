package cn.asany.crm.core.dao;

import cn.asany.crm.core.domain.Customer;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("crm.customerDao")
public interface CustomerDao extends JpaRepository<Customer, Long> {}
