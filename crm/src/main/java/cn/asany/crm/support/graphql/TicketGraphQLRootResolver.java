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
package cn.asany.crm.support.graphql;

import cn.asany.crm.support.convert.TicketConverter;
import cn.asany.crm.support.domain.Ticket;
import cn.asany.crm.support.graphql.input.TicketCreateInput;
import cn.asany.crm.support.graphql.input.TicketUpdateInput;
import cn.asany.crm.support.graphql.input.TicketWhereInput;
import cn.asany.crm.support.graphql.type.TicketConnection;
import cn.asany.crm.support.service.TicketService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.SpringSecurityUtils;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class TicketGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final TicketService ticketService;
  private final TicketConverter ticketConverter;

  public TicketGraphQLRootResolver(TicketService ticketService, TicketConverter ticketConverter) {
    this.ticketService = ticketService;
    this.ticketConverter = ticketConverter;
  }

  public TicketConnection ticketsConnection(
      TicketWhereInput where, int pageNumber, int pageSize, Sort orderBy) {
    Page<Ticket> page =
        this.ticketService.findPage(
            PageRequest.of(pageNumber - 1, pageSize, orderBy), where.toFilter());
    return Kit.connection(page, TicketConnection.class);
  }

  public List<Ticket> tickets(TicketWhereInput where, int offset, int limit, Sort sort) {
    return this.ticketService.findAll(where.toFilter(), offset, limit, sort);
  }

  public Optional<Ticket> ticket(Long id) {
    return this.ticketService.findById(id);
  }

  public Ticket createTicket(TicketCreateInput input) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    Ticket ticket = ticketConverter.toTicket(input);
    ticket.setCreatedBy(user.getUid());
    return this.ticketService.save(ticket);
  }

  public Ticket updateTicket(Long id, TicketUpdateInput input, Boolean merge) {
    Ticket ticket = ticketConverter.toTicket(input);
    return this.ticketService.update(id, ticket, merge);
  }

  public Optional<Ticket> deleteTicket(Long id) {
    return this.ticketService.delete(id);
  }

  public Optional<Ticket> assignTicket(Long id, Long userId) {
    return this.ticketService.assign(id, userId);
  }

  public Optional<Ticket> resolveTicket(Long id) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.ticketService.resolve(id, user.getUid());
  }

  public Optional<Ticket> reopenTicket(Long id) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.ticketService.reopen(id, user.getUid());
  }

  public Optional<Ticket> suspendTicket(Long id, String reason) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.ticketService.suspend(id, reason, user.getUid());
  }

  public Optional<Ticket> resumeTicket(Long id) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.ticketService.resume(id, user.getUid());
  }
}
