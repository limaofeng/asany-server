package cn.asany.crm.support.dao;

import cn.asany.crm.support.domain.TicketStatusLog;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketStatusLogDao extends JpaRepository<TicketStatusLog, Long> {}
