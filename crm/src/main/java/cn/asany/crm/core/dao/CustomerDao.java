package cn.asany.crm.core.dao;

import cn.asany.security.core.domain.User;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("crm.customerDao")
public interface CustomerDao extends JpaRepository<User, Long> {}
