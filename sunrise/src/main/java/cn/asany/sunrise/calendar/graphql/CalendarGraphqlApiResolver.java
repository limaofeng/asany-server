/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.sunrise.calendar.graphql;

import cn.asany.sunrise.calendar.convert.CalendarConverter;
import cn.asany.sunrise.calendar.convert.CalendarSetConverter;
import cn.asany.sunrise.calendar.domain.Calendar;
import cn.asany.sunrise.calendar.domain.CalendarAccount;
import cn.asany.sunrise.calendar.domain.CalendarEvent;
import cn.asany.sunrise.calendar.domain.CalendarSet;
import cn.asany.sunrise.calendar.domain.toys.CalendarEventDateStat;
import cn.asany.sunrise.calendar.graphql.input.*;
import cn.asany.sunrise.calendar.service.CalendarService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Date;
import java.util.List;
import net.asany.jfantasy.framework.error.ValidationException;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.SpringSecurityUtils;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Component;

/**
 * 日历 接口
 *
 * @author limaofeng
 */
@Component
public class CalendarGraphqlApiResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final CalendarService calendarService;
  private final CalendarConverter calendarConverter;
  private final CalendarSetConverter calendarSetConverter;

  public CalendarGraphqlApiResolver(
      CalendarService calendarService,
      CalendarConverter calendarConverter,
      CalendarSetConverter calendarSetConverter) {
    this.calendarService = calendarService;
    this.calendarConverter = calendarConverter;
    this.calendarSetConverter = calendarSetConverter;
  }

  public Calendar newCalendarSubscription(String url) {
    return this.calendarService.subscribe(url);
  }

  public List<CalendarEvent> calendarEvents(
      Date starts, Date ends, Long calendar, Long calendarSet) {
    if (calendarSet != null) {
      return this.calendarService.calendarEventsByCalendarSet(calendarSet, starts, ends);
    }
    if (calendar != null) {
      return this.calendarService.calendarEventsByCalendar(calendar, starts, ends);
    }
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.calendarService.calendarEventsByUid(user.getUid(), starts, ends);
  }

  public List<CalendarEvent> calendarEventsWithDays(
      Date date, Long days, Long calendar, Long calendarSet) {
    if (calendarSet != null) {
      return this.calendarService.calendarEventsWithDaysByCalendarSet(calendarSet, date, days);
    }
    if (calendar != null) {
      return this.calendarService.calendarEventsWithDaysByCalendar(calendar, date, days);
    }
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.calendarService.calendarEventsWithDaysByUid(user.getUid(), date, days);
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
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.calendarService.calendars(user.getUid());
  }

  public List<CalendarAccount> calendarAccounts() {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.calendarService.calendarAccounts(user.getUid());
  }

  public CalendarSet createCalendarSet(CalendarSetCreateInput input) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    CalendarSet calendarSet =
        ObjectUtil.defaultValue(calendarSetConverter.toCalendarSet(input), CalendarSet::new);
    return this.calendarService.createCalendarSet(user.getUid(), calendarSet.getName());
  }

  public CalendarSet updateCalendarSet(Long id, CalendarSetUpdateInput input, Boolean merge) {
    CalendarSet calendarSet = calendarSetConverter.toCalendarSet(input);
    return this.calendarService.updateCalendarSet(id, calendarSet, merge);
  }

  public Boolean deleteCalendarSet(Long id) {
    return this.calendarService.deleteCalendarSet(id);
  }

  public Calendar createCalendar(CalendarCreateInput input) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();

    if (!this.calendarService.verifyAccount(input.getAccount(), user)) {
      throw new ValidationException("日历账户错误");
    }

    return this.calendarService.createCalendar(input.getAccount(), input.getName());
  }

  public Calendar updateCalendar(Long id, CalendarUpdateInput input, Boolean merge) {
    Calendar calendar = calendarConverter.toCalendar(input);
    return this.calendarService.updateCalendar(id, calendar, merge);
  }

  public Boolean deleteCalendar(Long id) {
    return this.calendarService.deleteCalendar(id);
  }

  public CalendarSet addCalendarToSet(Long id, Long set) {
    return this.calendarService.addCalendarToSet(id, set);
  }

  public CalendarSet removeCalendarFromSet(Long id, Long set) {
    return this.calendarService.removeCalendarFromSet(id, set);
  }

  public CalendarEvent addCalendarEvent(Long calendar, CalendarEventCreateInput input) {
    CalendarEvent event = calendarConverter.toCalendarEvent(input);
    return this.calendarService.addCalendarEvent(calendar, event);
  }
}
