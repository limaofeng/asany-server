package cn.asany.sunrise.calendar.service;

import static java.util.regex.Pattern.compile;

import cn.asany.sunrise.calendar.bean.Calendar;
import cn.asany.sunrise.calendar.bean.CalendarEvent;
import cn.asany.sunrise.calendar.bean.enums.CalendarType;
import cn.asany.sunrise.calendar.bean.enums.Refresh;
import cn.asany.sunrise.calendar.dao.CalendarDao;
import cn.asany.sunrise.calendar.dao.CalendarEventDao;
import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.SneakyThrows;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Calendar Service
 *
 * @author limaofeng
 */
@Service
public class CalendarService {

  private static final CalendarBuilder BUILDER = new CalendarBuilder();

  private final CalendarDao calendarDao;
  private final CalendarEventDao calendarEventDao;

  public CalendarService(CalendarDao calendarDao, CalendarEventDao calendarEventDao) {
    this.calendarDao = calendarDao;
    this.calendarEventDao = calendarEventDao;
  }

  private List<CalendarEvent> parseEvents(
      net.fortuna.ical4j.model.Calendar icalendar, Calendar calendar) {
    List<CalendarEvent> events = new ArrayList<>();
    for (Object value : icalendar.getComponents(Component.VEVENT)) {
      VEvent event = (VEvent) value;

      CalendarEvent.CalendarEventBuilder builder = CalendarEvent.builder();

      boolean allDay = isAllDay(event);
      Date starts = ReflectionUtils.convert(event.getStartDate().getValue(), Date.class);
      Date ends = ReflectionUtils.convert(event.getEndDate().getValue(), Date.class);

      builder
          .datetime(allDay, starts, ends)
          .title(event.getSummary().getValue())
          .notes(text(event.getDescription()))
          .location(text(event.getLocation()))
          .calendar(calendar);

      // 创建时间
      if (null != event.getCreated()) {
        System.out.println("创建时间：" + event.getCreated().getValue());
      }
      // 最后修改时间
      if (null != event.getLastModified()) {
        System.out.println("最后修改时间：" + event.getLastModified().getValue());
      }
      // 重复规则
      if (null != event.getProperty("RRULE")) {
        System.out.println("RRULE:" + event.getProperty("RRULE").getValue());
      }
      // 提前多久提醒
      for (Object o : event.getAlarms()) {
        VAlarm alarm = (VAlarm) o;
        Pattern p = compile("[^0-9]");
        String aheadTime = alarm.getTrigger().getValue();
        Matcher m = p.matcher(aheadTime);
        int timeTemp = Integer.parseInt(m.replaceAll("").trim());
        if (aheadTime.endsWith("W")) {
          System.out.println("提前多久：" + timeTemp + "周");
        } else if (aheadTime.endsWith("D")) {
          System.out.println("提前多久：" + timeTemp + "天");
        } else if (aheadTime.endsWith("H")) {
          System.out.println("提前多久：" + timeTemp + "小时");
        } else if (aheadTime.endsWith("M")) {
          System.out.println("提前多久：" + timeTemp + "分钟");
        } else if (aheadTime.endsWith("S")) {
          System.out.println("提前多久：" + timeTemp + "秒");
        }
      }
      // 邀请人
      if (null != event.getProperty("ATTENDEE")) {
        ParameterList parameters = event.getProperty("ATTENDEE").getParameters();
        System.out.println(event.getProperty("ATTENDEE").getValue().split(":")[1]);
        System.out.println(parameters.getParameter("PARTSTAT").getValue());
      }
      events.add(builder.build());
    }
    return events;
  }

  public Optional<Calendar> findById(Long id) {
    return this.calendarDao.findById(id);
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
    net.fortuna.ical4j.model.Calendar icalendar = BUILDER.build(new URL(url).openStream());

    if (this.calendarDao.exists(PropertyFilter.builder().equal("url", url).build())) {
      throw new ValidationException("该地址已经订阅");
    }

    Calendar calendar =
        Calendar.builder()
            .type(CalendarType.SUBSCRIPTION)
            .name(text(icalendar.getProperty("X-WR-CALNAME")))
            .description(text(icalendar.getProperty("X-WR-CALDESC")))
            .url(url)
            .refresh(Refresh.EVERY_WEEK)
            .color(StringUtil.defaultValue(text(icalendar.getProperty("X-COLOR")), randomColor()))
            .version(text(icalendar.getVersion()))
            .build();

    List<CalendarEvent> events = parseEvents(icalendar, calendar);

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
    net.fortuna.ical4j.model.Calendar icalendar =
        BUILDER.build(new URL(calendar.getUrl()).openStream());

    if (calendar.getVersion().equals(icalendar.getVersion().getValue())) {
      return calendar;
    }

    calendar.setVersion(icalendar.getVersion().getValue());

    this.calendarEventDao.deleteEventsByCalendarId(calendar.getId());
    List<CalendarEvent> events = parseEvents(icalendar, calendar);
    this.calendarEventDao.saveAllInBatch(events);
    this.calendarDao.save(calendar);
    calendar.setEvents(events);
    return calendar;
  }

  private String randomColor() {
    Color color =
        Color.getHSBColor(
                new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat())
            .darker();
    return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
  }

  private String text(Property property) {
    if (property == null) {
      return null;
    }
    return property.getValue();
  }

  private boolean isAllDay(VEvent event) {
    if (null == event.getStartDate()) {
      return false;
    }
    ParameterList parameters = event.getStartDate().getParameters();
    Parameter parameter = parameters.getParameter("VALUE");
    return parameter != null && "DATE".equals(parameter.getValue());
  }

  public Optional<Calendar> findByUrl(String url) {
    return this.calendarDao.findOne(PropertyFilter.builder().equal("url", url).build());
  }
}
