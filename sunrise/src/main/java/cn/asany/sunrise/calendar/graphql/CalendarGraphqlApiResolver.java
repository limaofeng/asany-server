package cn.asany.sunrise.calendar.graphql;

import cn.asany.sunrise.calendar.bean.Calendar;
import cn.asany.sunrise.calendar.bean.CalendarEvent;
import cn.asany.sunrise.calendar.bean.CalendarSet;
import cn.asany.sunrise.calendar.service.CalendarService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 日历 接口
 *
 * @author limaofeng
 */
@Component
public class CalendarGraphqlApiResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final CalendarService calendarService;

  public CalendarGraphqlApiResolver(CalendarService calendarService) {
    this.calendarService = calendarService;
  }

  public List<CalendarEvent> calendarEvents(Date starts, Date ends) {
    return this.calendarService.calendarEvents(starts, ends);
  }

  public List<CalendarSet> calendarSets() {
    return this.calendarService.calendarSets();
  }

  public List<Calendar> calendars() {
    return this.calendarService.findAll();
  }
}
