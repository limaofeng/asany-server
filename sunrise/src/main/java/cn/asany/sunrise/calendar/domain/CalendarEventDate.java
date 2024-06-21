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
package cn.asany.sunrise.calendar.domain;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 事件日期 <br>
 * 如果跨天事件，每天一条记录
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "SUNRISE_CALENDAR_EVENT_DATE")
public class CalendarEventDate extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 日期 */
  @Temporal(TemporalType.DATE)
  @Column(updatable = false, name = "DATE")
  private Date date;

  /** 事件 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "EVENT_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_CALENDAR_EVENT_DATE_EVENT_ID"))
  @ToString.Exclude
  private CalendarEvent event;
}
