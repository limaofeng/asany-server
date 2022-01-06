package cn.asany.sunrise.calendar.graphql.resolver;

import cn.asany.sunrise.calendar.bean.CalendarEvent;
import graphql.kickstart.tools.GraphQLResolver;
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
}
