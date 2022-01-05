package cn.asany.sunrise.calendar.bean.toys;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
