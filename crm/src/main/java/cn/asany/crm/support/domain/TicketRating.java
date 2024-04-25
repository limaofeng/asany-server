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
package cn.asany.crm.support.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "CRM_TICKET_RATINGS")
public class TicketRating extends BaseBusEntity {

  @Id
  @Column(name = "TICKET_ID", nullable = false, updatable = false, precision = 22)
  @GenericGenerator(
      name = "ticketRatingPkGenerator",
      strategy = "foreign",
      parameters = {@org.hibernate.annotations.Parameter(name = "property", value = "ticket")})
  @GeneratedValue(generator = "ticketRatingPkGenerator")
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, targetEntity = Ticket.class, mappedBy = "rating")
  private Ticket ticket;

  /** 评价分数，例如，1到5 1: 非常差 2: 差 3: 一般 4: 好 5: 非常好 */
  @Column(name = "SCORE", nullable = false)
  private Integer score;

  /** 评价内容 */
  @Column(name = "COMMENT", length = 500)
  private String comment;

  /** 评价时间 */
  @Column(name = "RATING_TIME", nullable = false)
  private LocalDateTime ratingTime;
}
