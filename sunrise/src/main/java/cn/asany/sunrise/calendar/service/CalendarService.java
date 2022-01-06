package cn.asany.sunrise.calendar.service;

import cn.asany.sunrise.calendar.bean.Calendar;
import cn.asany.sunrise.calendar.bean.CalendarEvent;
import cn.asany.sunrise.calendar.bean.CalendarSet;
import cn.asany.sunrise.calendar.bean.enums.CalendarType;
import cn.asany.sunrise.calendar.bean.enums.Refresh;
import cn.asany.sunrise.calendar.dao.CalendarDao;
import cn.asany.sunrise.calendar.dao.CalendarEventDao;
import cn.asany.sunrise.calendar.dao.CalendarSetDao;
import cn.asany.sunrise.calendar.util.CalendarUtils;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Calendar Service
 *
 * @author limaofeng
 */
@Service
public class CalendarService {

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

  public List<CalendarSet> calendarSets() {
    return this.calendarSetDao.findAll();
  }

  public Optional<Calendar> findById(Long id) {
    return this.calendarDao.findById(id);
  }

  public List<CalendarEvent> calendarEvents(Date starts, Date ends) {
    return this.calendarEventDao.findAll(
        PropertyFilter.builder()
            .or(
                PropertyFilter.builder().between("datetime.starts", starts, ends),
                PropertyFilter.builder().between("datetime.ends", starts, ends))
            .build());
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
}
