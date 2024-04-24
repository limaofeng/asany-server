package cn.asany.crm.support.dao;

import cn.asany.crm.support.domain.TicketType;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeDao extends AnyJpaRepository<TicketType, Long> {}
