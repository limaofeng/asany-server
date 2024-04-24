package cn.asany.crm.support.dao;

import cn.asany.crm.support.domain.TicketStatusLog;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketStatusLogDao extends AnyJpaRepository<TicketStatusLog, Long> {}
