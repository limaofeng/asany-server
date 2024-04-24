package cn.asany.crm.support.service;

import cn.asany.crm.support.dao.TicketDao;
import cn.asany.crm.support.dao.TicketStatusLogDao;
import cn.asany.crm.support.domain.Ticket;
import cn.asany.crm.support.domain.TicketStatusLog;
import cn.asany.crm.support.domain.TicketType;
import cn.asany.crm.support.domain.enums.TicketStatus;
import cn.asany.security.core.domain.User;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.error.ValidationException;
import net.asany.jfantasy.framework.spring.SpELUtil;
import net.asany.jfantasy.framework.util.common.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

  private final TicketDao ticketDao;
  private final TicketStatusLogDao ticketStatusLogDao;

  private final TicketTypeService ticketTypeService;

  public TicketService(
      TicketDao ticketDao,
      TicketStatusLogDao ticketStatusLogDao,
      TicketTypeService ticketTypeService) {
    this.ticketDao = ticketDao;
    this.ticketStatusLogDao = ticketStatusLogDao;
    this.ticketTypeService = ticketTypeService;
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
    TicketType ticketType =
        ticketTypeService
            .findById(ticket.getType().getId())
            .orElseThrow(() -> new ValidationException("Ticket Type not found"));

    if (ticket.getPriority() == null) {
      ticket.setPriority(ticketType.getDefaultPriority());
    }

    ticket.setNo(generateNo(ticket, ticketType.getNumberingTemplate()));
    ticket.setStatus(TicketStatus.NEW);

    Ticket newTicket = this.ticketDao.save(ticket);
    this.ticketStatusLogDao.save(
        TicketStatusLog.builder()
            .status(ticket.getStatus())
            .logTime(DateUtil.now())
            .loggedBy(User.builder().id(ticket.getCreatedBy()).build())
            .ticket(newTicket)
            .build());
    return newTicket;
  }

  private String generateNo(Ticket ticket, String numberingTemplate) {
    SpelExpressionParser parser = new SpelExpressionParser();
    Expression expression = parser.parseExpression(numberingTemplate);
    return expression.getValue(SpELUtil.createEvaluationContext(ticket), String.class);
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

  public Optional<Ticket> resume(Long id, Long uid) {
    return this.ticketDao
        .findById(id)
        .map(
            ticket -> {
              ticket.setStatus(TicketStatus.IN_PROGRESS);
              this.ticketDao.save(ticket);
              this.ticketStatusLogDao.save(
                  TicketStatusLog.builder()
                      .status(TicketStatus.IN_PROGRESS)
                      .logTime(DateUtil.now())
                      .loggedBy(User.builder().id(uid).build())
                      .ticket(ticket)
                      .build());
              return ticket;
            });
  }
}
