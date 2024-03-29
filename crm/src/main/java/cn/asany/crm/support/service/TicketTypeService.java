package cn.asany.crm.support.service;

import cn.asany.crm.support.dao.TicketTypeDao;
import cn.asany.crm.support.domain.TicketType;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketTypeService {

  private final TicketTypeDao ticketTypeDao;

  public TicketTypeService(TicketTypeDao ticketTypeDao) {
    this.ticketTypeDao = ticketTypeDao;
  }

  @Transactional(readOnly = true)
  public Optional<TicketType> findById(Long id) {
    return this.ticketTypeDao.findById(id);
  }
}
