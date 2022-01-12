package cn.asany.sunrise.calendar.bean.toys;

import cn.asany.sunrise.calendar.bean.CalendarEventDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.util.common.DateUtil;

/**
 * 事件时间
 *
 * @author limaofeng
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class EventTime {
  /** 全天 */
  @Column(name = "ALL_DAY")
  private Boolean allDay;
  /** 开始时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "`STARTS`")
  private Date starts;
  /** 结束时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "`ENDS`")
  private Date ends;
  /** 时间覆盖到的所有天 */
  @OneToMany(
      mappedBy = "event",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @ToString.Exclude
  private List<CalendarEventDate> dates;

  public static EventTimeBuilder builder() {
    return new EventTimeBuilder() {
      @Override
      public EventTime build() {
        EventTime time = super.build();
        List<Date> dates = DateUtil.betweenDates(time.getStarts(), time.getEnds(), Calendar.DATE);
        time.setDates(
            dates.stream()
                .map(item -> CalendarEventDate.builder().date(item).build())
                .collect(Collectors.toList()));
        return time;
      }
    };
  }
}
