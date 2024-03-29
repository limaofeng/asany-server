package cn.asany.crm.support.dao;

import cn.asany.crm.support.domain.TicketType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeDao extends JpaRepository<TicketType, Long> {}
