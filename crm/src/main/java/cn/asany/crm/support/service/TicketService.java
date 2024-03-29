package cn.asany.crm.support.service;

import cn.asany.crm.support.dao.TicketDao;
import cn.asany.crm.support.dao.TicketStatusLogDao;
import cn.asany.crm.support.domain.Ticket;
import cn.asany.crm.support.domain.TicketStatusLog;
import cn.asany.crm.support.domain.enums.TicketStatus;
import cn.asany.security.core.domain.User;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.util.common.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

  private final TicketDao ticketDao;
  private final TicketStatusLogDao ticketStatusLogDao;

  public TicketService(TicketDao ticketDao, TicketStatusLogDao ticketStatusLogDao) {
    this.ticketDao = ticketDao;
    this.ticketStatusLogDao = ticketStatusLogDao;
  }

  @Transactional(readOnly = true)
  public Page<Ticket> findPage(Pageable pageable, PropertyFilter filter) {
    return this.ticketDao.findPage(pageable, filter);
  }

  @Transactional(readOnly = true)
  public List<Ticket> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return this.ticketDao.findAll(filter, offset, limit, sort);
  }

  @Transactional(readOnly = true)
  public Optional<Ticket> findById(Long id) {
    return this.ticketDao.findById(id);
  }

  @Transactional
  public Ticket save(Ticket ticket) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    Ticket newTicket = this.ticketDao.save(ticket);
    this.ticketStatusLogDao.save(
        TicketStatusLog.builder()
            .status(TicketStatus.NEW)
            .logTime(DateUtil.now())
            .loggedBy(User.builder().id(user.getUid()).build())
            .ticket(newTicket)
            .build());
    return newTicket;
  }

  @Transactional
  public Ticket update(Long id, Ticket ticket, Boolean merge) {
    ticket.setId(id);
    return this.ticketDao.update(ticket, merge);
  }

  @Transactional
  public Optional<Ticket> delete(Long id) {
    return this.ticketDao
        .findById(id)
        .map(
            ticket -> {
              this.ticketDao.delete(ticket);
              return ticket;
            });
  }

  public Optional<Ticket> assign(Long id, Long userId) {
    return this.ticketDao
        .findById(id)
        .map(
            ticket -> {
              ticket.setAssignee(User.builder().id(userId).build());
              ticket.setStatus(TicketStatus.IN_PROGRESS);
              this.ticketDao.save(ticket);
              this.ticketStatusLogDao.save(
                  TicketStatusLog.builder()
                      .status(TicketStatus.IN_PROGRESS)
                      .logTime(DateUtil.now())
                      .loggedBy(User.builder().id(userId).build())
                      .ticket(ticket)
                      .build());
              return ticket;
            });
  }

  public Optional<Ticket> resolve(Long id, Long userId) {
    return this.ticketDao
        .findById(id)
        .map(
            ticket -> {
              ticket.setStatus(TicketStatus.RESOLVED);
              this.ticketDao.save(ticket);
              this.ticketStatusLogDao.save(
                  TicketStatusLog.builder()
                      .status(TicketStatus.RESOLVED)
                      .logTime(DateUtil.now())
                      .loggedBy(User.builder().id(userId).build())
                      .ticket(ticket)
                      .build());
              return ticket;
            });
  }

  public Optional<Ticket> reopen(Long id, Long userId) {
    return this.ticketDao
        .findById(id)
        .map(
            ticket -> {
              ticket.setStatus(TicketStatus.REOPEN);
              this.ticketDao.save(ticket);
              this.ticketStatusLogDao.save(
                  TicketStatusLog.builder()
                      .status(TicketStatus.REOPEN)
                      .logTime(DateUtil.now())
                      .loggedBy(User.builder().id(userId).build())
                      .ticket(ticket)
                      .build());
              return ticket;
            });
  }

  public Optional<Ticket> suspend(Long id, String reason, Long userId) {
    return this.ticketDao
        .findById(id)
        .map(
            ticket -> {
              ticket.setStatus(TicketStatus.SUSPENDED);
              this.ticketDao.save(ticket);
              this.ticketStatusLogDao.save(
                  TicketStatusLog.builder()
                      .note(reason)
                      .status(TicketStatus.SUSPENDED)
                      .logTime(DateUtil.now())
                      .loggedBy(User.builder().id(userId).build())
                      .ticket(ticket)
                      .build());
              return ticket;
            });
  }
}
