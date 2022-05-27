package cn.asany.sunrise.calendar.graphql.resolver;

import cn.asany.sunrise.calendar.domain.CalendarEvent;
import cn.asany.sunrise.calendar.domain.CalendarEventDate;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * CalendarEvent GraphQLResolver
 *
 * @author limaofeng
 */
@Component
public class CalendarEventGraphqlResolver implements GraphQLResolver<CalendarEvent> {

  public String color(CalendarEvent event) {
    return event.getCalendar().getColor();
  }

  public List<Date> dates(CalendarEvent event) {
    return event.getDates().stream().map(CalendarEventDate::getDate).collect(Collectors.toList());
  }
}
