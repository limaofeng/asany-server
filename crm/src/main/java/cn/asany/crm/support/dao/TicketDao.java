package cn.asany.crm.support.dao;

import cn.asany.crm.support.domain.Ticket;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketDao extends AnyJpaRepository<Ticket, Long> {}
