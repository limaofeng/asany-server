package cn.asany.sunrise.calendar.graphql;

import cn.asany.sunrise.calendar.bean.Calendar;
import cn.asany.sunrise.calendar.bean.CalendarEvent;
import cn.asany.sunrise.calendar.bean.CalendarSet;
import cn.asany.sunrise.calendar.bean.toys.CalendarEventDateStat;
import cn.asany.sunrise.calendar.service.CalendarService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Date;
import java.util.List;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
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

  public Calendar newCalendarSubscription(String url) {
    return this.calendarService.subscribe(url);
  }

  public List<CalendarEvent> calendarEvents(
      Date starts, Date ends, Long calendar, Long calendarSet) {
    return this.calendarService.calendarEvents(starts, ends, calendar, calendarSet);
  }

  public List<CalendarEvent> calendarEventsWithDays(
      Date date, Long days, Long calendar, Long calendarSet) {
    return this.calendarService.calendarEventsWithDays(date, days, calendar, calendarSet);
  }

  public List<CalendarSet> calendarSets() {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.calendarService.calendarSets(user.getUid());
  }

  public List<CalendarEventDateStat> calendarEventDates(
      Date starts, Date ends, Long calendar, Long calendarSet) {
    if (calendarSet != null) {
      return this.calendarService.calendarEventDatesByCalendarSet(calendarSet, starts, ends);
    }
    if (calendar != null) {
      return this.calendarService.calendarEventDatesByCalendar(calendar, starts, ends);
    }
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.calendarService.calendarEventDates(user.getUid(), starts, ends);
  }

  public List<Calendar> calendars() {
    return this.calendarService.findAll();
  }
}
