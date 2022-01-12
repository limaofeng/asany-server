package cn.asany.sunrise.calendar.bean;

import cn.asany.sunrise.calendar.bean.toys.EventTime;
import cn.asany.sunrise.calendar.bean.toys.Remind;
import cn.asany.sunrise.calendar.bean.toys.Repeat;
import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
  @JoinColumn(name = "CALENDAR_ID")
  @ToString.Exclude
  private Calendar calendar;

  public static CalendarEventBuilder builder() {
    return new CalendarEventBuilder() {
      @Override
      public CalendarEvent build() {
        CalendarEvent event = super.build();
        for (CalendarEventDate date : event.getDatetime().getDates()) {
          date.setEvent(event);
        }
        return event;
      }
    };
  }

  public static class CalendarEventBuilder {

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
