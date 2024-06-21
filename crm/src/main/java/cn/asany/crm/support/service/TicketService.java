/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.crm.support.service;

import cn.asany.crm.core.dao.CustomerDao;
import cn.asany.crm.core.dao.CustomerStoreDao;
import cn.asany.crm.core.domain.Customer;
import cn.asany.crm.core.domain.CustomerStore;
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
import org.hibernate.Hibernate;
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

  private final CustomerDao customerDao;

  private final CustomerStoreDao customerStoreDao;

  private final TicketTypeService ticketTypeService;

  public TicketService(
      TicketDao ticketDao,
      TicketStatusLogDao ticketStatusLogDao,
      CustomerDao customerDao,
      CustomerStoreDao customerStoreDao,
      TicketTypeService ticketTypeService) {
    this.ticketDao = ticketDao;
    this.ticketStatusLogDao = ticketStatusLogDao;
    this.ticketTypeService = ticketTypeService;
    this.customerDao = customerDao;
    this.customerStoreDao = customerStoreDao;
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
    return this.ticketDao
        .findById(id)
        .map(
            ticket -> {
              Hibernate.initialize(ticket.getStore());
              Hibernate.initialize(ticket.getType());
              return ticket;
            });
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

    CustomerStore store = this.customerStoreDao.getReferenceById(ticket.getStore().getId());
    Customer customer = this.customerDao.getReferenceById(ticket.getCustomer().getId());

    Hibernate.initialize(customer);
    Hibernate.initialize(store);

    ticket.setStore(store);
    ticket.setCustomer(customer);
    ticket.setNo(generateNo(ticket, ticketType.getNumberingTemplate()));
    ticket.setStatus(TicketStatus.NEW);

    Ticket newTicket = this.ticketDao.save(ticket);
    this.ticketStatusLogDao.save(
        TicketStatusLog.builder()
            .status(ticket.getStatus())
            .logTime(DateUtil.now())
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
