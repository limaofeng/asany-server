package cn.asany.sunrise.calendar.domain.toys;

import java.util.Date;
import javax.persistence.*;
import lombok.*;

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

  //  public static EventTimeBuilder builder() {
  //    return new EventTimeBuilder() {
  //      @Override
  //      public EventTime build() {
  //        EventTime time = super.build();
  //
  //        return time;
  //      }
  //    };
  //  }
}
