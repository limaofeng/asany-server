package cn.asany.sunrise.calendar.graphql.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarCreateInput {
  private String name;
  private Long account;
}
