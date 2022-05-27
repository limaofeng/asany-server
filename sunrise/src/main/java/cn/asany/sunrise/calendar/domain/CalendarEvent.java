package cn.asany.sunrise.calendar.domain;

import cn.asany.sunrise.calendar.domain.toys.EventTime;
import cn.asany.sunrise.calendar.domain.toys.Remind;
import cn.asany.sunrise.calendar.domain.toys.Repeat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.common.DateUtil;

/**
 * 日历事件
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@NamedEntityGraph(
    name = "Graph.CalendarEvent.FetchDates",
    attributeNodes = {
      @NamedAttributeNode(value = "dates", subgraph = "SubGraph.CalendarEventDates"),
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.CalendarEventDates",
          attributeNodes = {
            @NamedAttributeNode(value = "date"),
          }),
    })
@Entity
@Table(name = "SUNRISE_CALENDAR_EVENT")
public class CalendarEvent extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 标题 */
  @Column(name = "TITLE", length = 200)
  private String title;
  /** 时间时间 */
  @Embedded private EventTime datetime;
  /** 循环设置 */
  @Embedded private Repeat repeat;
  /** 提醒 */
  @Embedded private Remind remind;
  /** 说明 */
  @Column(name = "NOTES", columnDefinition = "TEXT")
  private String notes;
  /** 地点 */
  @Column(name = "LOCATION", length = 100)
  private String location;
  /** 链接 */
  @Column(name = "URL", length = 120)
  private String url;
  /** 日历 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "CALENDAR_ID",
      foreignKey = @ForeignKey(name = "FK_CALENDAR_EVENT_CALENDAR_ID"),
      nullable = false)
  @ToString.Exclude
  private Calendar calendar;

  /** 时间覆盖到的所有天 */
  @OneToMany(
      mappedBy = "event",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @ToString.Exclude
  private List<CalendarEventDate> dates;

  public static CalendarEventBuilder builder() {
    return new CalendarEventBuilder() {
      @Override
      public CalendarEvent build() {
        CalendarEvent event = super.build();
        EventTime time = event.getDatetime();
        List<Date> dates =
            DateUtil.betweenDates(time.getStarts(), time.getEnds(), java.util.Calendar.DATE);
        event.setDates(
            dates.stream()
                .map(item -> CalendarEventDate.builder().date(item).event(event).build())
                .collect(Collectors.toList()));
        return event;
      }
    };
  }

  public static class CalendarEventBuilder {

    /**
     * 时间设置
     *
     * @param datetime 事件时间
     * @return CalendarEventBuilder
     */
    public CalendarEventBuilder datetime(EventTime datetime) {
      this.datetime = datetime;
      return this;
    }

    /**
     * 时间设置
     *
     * @param allDay 是否为全天时间
     * @param starts 开始时间
     * @param ends 结束时间
     * @return CalendarEventBuilder
     */
    public CalendarEventBuilder datetime(boolean allDay, Date starts, Date ends) {
      this.datetime = EventTime.builder().allDay(allDay).starts(starts).ends(ends).build();
      return this;
    }
  }
}
