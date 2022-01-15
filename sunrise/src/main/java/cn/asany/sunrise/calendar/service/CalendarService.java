package cn.asany.sunrise.calendar.service;

import cn.asany.sunrise.calendar.bean.Calendar;
import cn.asany.sunrise.calendar.bean.CalendarEvent;
import cn.asany.sunrise.calendar.bean.CalendarSet;
import cn.asany.sunrise.calendar.bean.enums.CalendarType;
import cn.asany.sunrise.calendar.bean.enums.Refresh;
import cn.asany.sunrise.calendar.bean.toys.CalendarEventDateStat;
import cn.asany.sunrise.calendar.bean.toys.DateRange;
import cn.asany.sunrise.calendar.dao.CalendarDao;
import cn.asany.sunrise.calendar.dao.CalendarEventDao;
import cn.asany.sunrise.calendar.dao.CalendarSetDao;
import cn.asany.sunrise.calendar.util.CalendarUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.error.ValidationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Calendar Service
 *
 * @author limaofeng
 */
@Service
public class CalendarService {

  private static final String DEFAULT_CALENDAR_SET_NAME = "未命名日历集";

  private final CalendarDao calendarDao;
  private final CalendarSetDao calendarSetDao;
  private final CalendarEventDao calendarEventDao;

  public CalendarService(
      CalendarDao calendarDao, CalendarEventDao calendarEventDao, CalendarSetDao calendarSetDao) {
    this.calendarDao = calendarDao;
    this.calendarEventDao = calendarEventDao;
    this.calendarSetDao = calendarSetDao;
  }

  public Optional<Calendar> findByUrl(String url) {
    return this.calendarDao.findOne(PropertyFilter.builder().equal("url", url).build());
  }

  public List<Calendar> findAll() {
    return this.calendarDao.findAll();
  }

  public List<CalendarSet> calendarSets(Long uid) {
    return this.calendarSetDao.findAll(PropertyFilter.builder().equal("owner.id", uid).build());
  }

  public Optional<Calendar> findById(Long id) {
    return this.calendarDao.findById(id);
  }

  /**
   * 日历事件
   *
   * @param starts 开始时间
   * @param ends 结束时间
   * @param calendar 日历
   * @param calendarSet 日历集
   * @return List<CalendarEvent>
   */
  public List<CalendarEvent> calendarEvents(
      Date starts, Date ends, Long calendar, Long calendarSet) {
    PropertyFilterBuilder builder =
        PropertyFilter.builder().between("datetime.dates.date", starts, ends);
    if (calendarSet == null && calendar == null) {
      // TODO: 获取可以访问(可见)的日历
      //  builder.in("calendar.id");
    } else {
      if (calendarSet != null) {
        builder.equal("calendar.calendarSets.id", calendarSet);
      }
      if (calendar != null) {
        builder.equal("calendar.id", calendar);
      }
    }
    return this.calendarEventDao.findAll(builder.build(), Sort.by("datetime.starts").ascending());
  }

  public List<CalendarEvent> calendarEventsWithDays(
      Date date, Long days, Long calendar, Long calendarSet) {
    if (calendarSet != null) {
      return calendarEventsByByCalendarSet(calendarSet, date, days.intValue() / 2);
    }
    return new ArrayList<>();
  }

  /**
   * 查询日历事件
   *
   * @param calendarSet 日历集
   * @param date 日期
   * @param day 前后天数
   * @return List<CalendarEvent>
   */
  public List<CalendarEvent> calendarEventsByByCalendarSet(Long calendarSet, Date date, int day) {
    DateRange range =
        this.calendarEventDao.calendarEventDateStartAndEndByCalendarSet(calendarSet, date, day);
    return this.calendarEvents(range.getStart(), range.getEnd(), null, calendarSet);
  }

  /**
   * 日历集统计
   *
   * @param calendarSet 日历集
   * @param starts 开始时间
   * @param ends 结束时间
   * @return List<CalendarEventDateStat>
   */
  public List<CalendarEventDateStat> calendarEventDatesByCalendarSet(
      Long calendarSet, Date starts, Date ends) {
    return this.calendarEventDao.calendarEventDatesByCalendarSet(calendarSet, starts, ends);
  }

  /**
   * 日历集统计
   *
   * @param calendar 日历
   * @param starts 开始时间
   * @param ends 结束时间
   * @return List<CalendarEventDateStat>
   */
  public List<CalendarEventDateStat> calendarEventDatesByCalendar(
      Long calendar, Date starts, Date ends) {
    return this.calendarEventDao.calendarEventDatesByCalendar(calendar, starts, ends);
  }

  /**
   * 日历集统计 (全部)
   *
   * @param uid 用户
   * @param starts 开始时间
   * @param ends 结束时间
   * @return List<CalendarEventDateStat>
   */
  public List<CalendarEventDateStat> calendarEventDates(Long uid, Date starts, Date ends) {
    return this.calendarEventDao.calendarEventDates(uid, starts, ends);
  }

  /**
   * 订阅日历
   *
   * @param url 地址
   * @return Calendar
   */
  @SneakyThrows
  @Transactional(rollbackFor = RuntimeException.class)
  public Calendar subscribe(String url) {
    net.fortuna.ical4j.model.Calendar icalendar = CalendarUtils.loadRemote(url);

    if (this.calendarDao.exists(PropertyFilter.builder().equal("url", url).build())) {
      throw new ValidationException("该地址已经订阅");
    }

    Calendar calendar =
        Calendar.builder()
            .type(CalendarType.SUBSCRIPTION)
            .name(CalendarUtils.getProperty(icalendar, "X-WR-CALNAME"))
            .description(CalendarUtils.getProperty(icalendar, "X-WR-CALDESC"))
            .url(url)
            .refresh(Refresh.EVERY_WEEK)
            .color(CalendarUtils.getColor(icalendar))
            .build();

    List<CalendarEvent> events = CalendarUtils.parseEvents(icalendar, calendar);

    this.calendarDao.save(calendar);
    this.calendarEventDao.saveAllInBatch(events);
    calendar.setEvents(events);
    return calendar;
  }

  /**
   * 刷新日历
   *
   * @param id 日历ID
   * @return Calendar
   */
  @SneakyThrows
  @Transactional(rollbackFor = RuntimeException.class)
  public Calendar refresh(Long id) {
    Optional<Calendar> optional = this.calendarDao.findById(id);
    if (!optional.isPresent()) {
      throw new ValidationException(String.format("日历[%d]订阅不存在", id));
    }
    Calendar calendar = optional.get();
    if (calendar.getType() != CalendarType.SUBSCRIPTION) {
      throw new ValidationException(String.format("日历[%d]不是订阅日历", id));
    }
    if (calendar.getRefresh() == Refresh.NEVER) {
      return calendar;
    }
    net.fortuna.ical4j.model.Calendar icalendar = CalendarUtils.loadRemote(calendar.getUrl());

    this.calendarEventDao.deleteEventsByCalendarId(calendar.getId());
    List<CalendarEvent> events = CalendarUtils.parseEvents(icalendar, calendar);
    this.calendarEventDao.saveAllInBatch(events);
    this.calendarDao.save(calendar);
    calendar.setEvents(events);
    return calendar;
  }

  public void delete(Long id) {
    this.calendarDao.deleteById(id);
  }

  public void createCalendarSet() {
    CalendarSet calendarSet = CalendarSet.builder().build();
    calendarSet.setName("未命名日历集");
    this.calendarSetDao.save(calendarSet);
  }

  public void updateCalendarSet() {}
}
