package cn.asany.sunrise.calendar.graphql.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarSetUpdateInput {
  private String name;
  private Integer index;
  private Long defaultCalendar;
}
