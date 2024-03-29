package cn.asany.crm.support.dao;

import cn.asany.crm.support.domain.Ticket;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketDao extends JpaRepository<Ticket, Long> {}
