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

import cn.asany.sunrise.calendar.domain.enums.CalendarType;
import cn.asany.sunrise.calendar.domain.enums.Refresh;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 日历
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NamedEntityGraph(
    name = "Graph.Calendar.FetchAccount",
    attributeNodes = {
      @NamedAttributeNode(value = "account", subgraph = "SubGraph.CalendarAccount"),
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.CalendarAccount",
          attributeNodes = {
            @NamedAttributeNode(value = "name"),
            @NamedAttributeNode(value = "index")
          }),
    })
@Entity
@Table(name = "SUNRISE_CALENDAR")
public class Calendar extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 日历类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private CalendarType type;

  /** 名称 */
  @Column(name = "NAME", length = 50, nullable = false)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 颜色 */
  @Column(name = "COLOR", length = 10)
  private String color;

  /** 订阅地址 */
  @Column(name = "URL", length = 200)
  private String url;

  /** 排序字段 */
  @Column(name = "SORT")
  private Integer index;

  /** 刷新设置 */
  @Enumerated(EnumType.STRING)
  @Column(name = "REFRESH", length = 10)
  private Refresh refresh;

  /** 版本 */
  @Version
  @Column(name = "VERSION")
  private Long version;

  /** 事件 */
  @OneToMany(
      mappedBy = "calendar",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @ToString.Exclude
  private List<CalendarEvent> events;

  /** 关联的日历集 */
  @ToString.Exclude
  @ManyToMany(targetEntity = CalendarSet.class, mappedBy = "calendars", fetch = FetchType.LAZY)
  private List<CalendarSet> calendarSets;

  /** 日历账号 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "CALENDAR_ACCOUNT",
      foreignKey = @ForeignKey(name = "FK_CALENDAR_CALENDAR_ACCOUNT_ID"))
  @ToString.Exclude
  private CalendarAccount account;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Calendar calendar = (Calendar) o;
    return id != null && Objects.equals(id, calendar.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
