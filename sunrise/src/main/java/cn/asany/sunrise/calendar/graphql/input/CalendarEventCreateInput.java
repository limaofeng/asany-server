package cn.asany.sunrise.calendar.graphql.input;

import cn.asany.sunrise.calendar.domain.toys.Remind;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventCreateInput {
  /** 标题 */
  private String title;
  /** 全天 */
  private Boolean allDay;
  /** 开始时间 */
  private Date starts;
  /** 结束时间 */
  private Date ends;
  /** 提醒 */
  private Remind remind;
  /** 说明 */
  private String notes;
  /** 地点 */
  private String location;
  /** 链接 */
  private String url;
}
