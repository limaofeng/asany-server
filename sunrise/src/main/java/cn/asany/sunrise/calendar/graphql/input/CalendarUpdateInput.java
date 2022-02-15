package cn.asany.sunrise.calendar.graphql.input;

import cn.asany.sunrise.calendar.bean.enums.Refresh;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarUpdateInput {
  /** 名称 */
  private String name;
  /** 排序 */
  private Integer index;
  /** 订阅地址 */
  private String url;
  /** 颜色 */
  private String color;
  /** 订阅刷新设置 */
  private Refresh refresh;
}
