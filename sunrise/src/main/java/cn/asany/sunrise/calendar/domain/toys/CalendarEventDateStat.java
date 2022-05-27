package cn.asany.sunrise.calendar.domain.toys;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日历事件时间统计
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventDateStat {
  private Date date;
  private Long number;
}
