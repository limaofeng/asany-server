package cn.asany.sunrise.calendar.service;

import cn.asany.security.core.domain.User;
import cn.asany.sunrise.calendar.dao.CalendarAccountDao;
import cn.asany.sunrise.calendar.dao.CalendarDao;
import cn.asany.sunrise.calendar.dao.CalendarEventDao;
import cn.asany.sunrise.calendar.dao.CalendarSetDao;
import cn.asany.sunrise.calendar.domain.Calendar;
import cn.asany.sunrise.calendar.domain.CalendarAccount;
import cn.asany.sunrise.calendar.domain.CalendarEvent;
import cn.asany.sunrise.calendar.domain.CalendarSet;
import cn.asany.sunrise.calendar.domain.enums.Alert;
import cn.asany.sunrise.calendar.domain.enums.CalendarType;
import cn.asany.sunrise.calendar.domain.enums.Refresh;
import cn.asany.sunrise.calendar.domain.enums.RepeatType;
import cn.asany.sunrise.calendar.domain.toys.CalendarEventDateStat;
import cn.asany.sunrise.calendar.domain.toys.DateRange;
import cn.asany.sunrise.calendar.domain.toys.Remind;
import cn.asany.sunrise.calendar.domain.toys.Repeat;
import cn.asany.sunrise.calendar.util.CalendarUtils;
import java.util.*;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
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

  private static final String CALENDAR_SET_DEFAULT_NAME = "未命名日历集";
  private static final String CALENDAR_DEFAULT_NAME = "未命名";

  private final CalendarDao calendarDao;
  private final CalendarAccountDao calendarAccountDao;
  private final CalendarSetDao calendarSetDao;
  private final CalendarEventDao calendarEventDao;

  public CalendarService(
      CalendarDao calendarDao,
      CalendarAccountDao calendarAccountDao,
      CalendarEventDao calendarEventDao,
      CalendarSetDao calendarSetDao) {
    this.calendarDao = calendarDao;
    this.calendarAccountDao = calendarAccountDao;
    this.calendarEventDao = calendarEventDao;
    this.calendarSetDao = calendarSetDao;
  }

  public Optional<Calendar> findByUrl(String url) {
    return this.calendarDao.findOne(PropertyFilter.newFilter().equal("url", url));
  }

  public List<CalendarAccount> calendarAccounts(Long uid) {
    return this.calendarAccountDao.findAll(PropertyFilter.newFilter().equal("owner.id", uid));
  }

  public List<Calendar> calendars(Long uid) {
    return this.calendarDao.findAllWithAccountByUid(uid);
  }

  public List<CalendarSet> calendarSets(Long uid) {
    return this.calendarSetDao.findAll(
        PropertyFilter.newFilter().equal("owner.id", uid), Sort.by("index").ascending());
  }

  public Optional<Calendar> findById(Long id) {
    return this.calendarDao.findById(id);
  }

  public List<CalendarEvent> calendarEventsWithDaysByCalendarSet(
      Long calendarSet, Date date, Long days) {
    DateRange range =
        this.calendarEventDao.calendarEventDateStartAndEndByCalendarSet(
            calendarSet, date, days.intValue() / 2);
    return calendarEventsByCalendarSet(calendarSet, range.getStart(), range.getEnd());
  }

  /**
   * 日历事件
   *
   * @param calendarSet 日历集
   * @param starts 开始时间
   * @param ends 结束时间
   * @return List<CalendarEvent>
   */
  public List<CalendarEvent> calendarEventsByCalendarSet(Long calendarSet, Date starts, Date ends) {
    PropertyFilter filter = PropertyFilter.newFilter().between("dates.date", starts, ends);
    filter.equal("calendar.calendarSets.id", calendarSet);
    return this.calendarEventDao.findAllWithDates(
        filter, Sort.by("datetime.starts").ascending());
  }

  public List<CalendarEvent> calendarEventsWithDaysByCalendar(Long calendar, Date date, Long days) {
    // return calendarEventsByByCalendar(calendar, date, days.intValue() / 2);
    System.out.println(calendar + "\t" + DateUtil.format(date) + "\t" + days);
    return new ArrayList<>();
  }

  public List<CalendarEvent> calendarEventsByCalendar(Long calendar, Date starts, Date ends) {
    return new ArrayList<>();
  }

  /**
   * 查询日历事件
   *
   * @param uid 用户ID
   * @param date 日期
   * @param days 前后天数
   * @return List<CalendarEvent>
   */
  public List<CalendarEvent> calendarEventsWithDaysByUid(Long uid, Date date, Long days) {
    DateRange range =
        this.calendarEventDao.calendarEventDateStartAndEndByUid(uid, date, days.intValue() / 2);
    return calendarEventsByUid(uid, range.getStart(), range.getEnd());
  }

  public List<CalendarEvent> calendarEventsByUid(Long uid, Date starts, Date ends) {
    PropertyFilter filter = PropertyFilter.newFilter().between("dates.date", starts, ends);
    filter.equal("calendar.account.owner.id", uid);
    return this.calendarEventDao.findAllWithDates(
        filter, Sort.by("datetime.starts").ascending());
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

    if (this.calendarDao.exists(PropertyFilter.newFilter().equal("url", url))) {
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

  public Calendar createCalendar(Long account, String name) {
    Calendar.CalendarBuilder builder = Calendar.builder();
    if (StringUtil.isBlank(name)) {
      int i = 0;
      boolean isExist;
      do {
        name = CALENDAR_DEFAULT_NAME + (i != 0 ? " (" + i + ")" : "");
        isExist =
            this.calendarDao.exists(
                PropertyFilter.newFilter().equal("account.id", account).equal("name", name));
        i++;
      } while (isExist);
      builder.name(name);
    }
    builder
        .type(CalendarType.SUNRISE)
        .index(this.calendarDao.getMaxIndex(account) + 1)
        .color(CalendarUtils.randomColor())
        .account(CalendarAccount.builder().id(account).build());
    return this.calendarDao.save(builder.build());
  }

  @Transactional
  public Calendar updateCalendar(Long id, Calendar calendar, Boolean merge) {
    calendar.setId(id);
    Integer index = calendar.getIndex();

    if (index != null) {
      Calendar source = this.calendarDao.getReferenceById(id);
      Integer sourceIndex = source.getIndex();
      List<Calendar> calendars =
          this.calendarDao.findAll(
              PropertyFilter.newFilter().equal("account.id", source.getAccount().getId()),
              Sort.by("index").ascending());
      if (calendars.isEmpty()) {
        calendar.setIndex(1);
      } else {
        int maxIndex = calendars.get(calendars.size() - 1).getIndex();
        index = Math.max(Math.min(maxIndex, index), 0);
        int startIndex = Math.min(sourceIndex, index);
        int endIndex = Math.max(sourceIndex, index);
        List<Calendar> siblings =
            calendars.subList(startIndex - 1, endIndex).stream()
                .filter(item -> !item.getId().equals(id))
                .collect(Collectors.toList());
        rearrangeCalendars(siblings, startIndex, endIndex, sourceIndex > index);
        calendar.setIndex(index);
      }
    }

    return this.calendarDao.update(calendar, merge);
  }

  public Boolean deleteCalendar(Long id) {
    Calendar source = this.calendarDao.getReferenceById(id);
    Integer sourceIndex = source.getIndex();

    List<Calendar> calendars =
        this.calendarDao.findAll(
            PropertyFilter.newFilter().equal("account.id", source.getAccount().getId()),
            Sort.by("index").ascending());

    rearrangeCalendars(calendars, sourceIndex, calendars.size(), false);

    this.calendarDao.delete(source);

    return true;
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

  public CalendarSet createCalendarSet(Long uid) {
    return this.createCalendarSet(uid, null);
  }

  public CalendarSet createCalendarSet(Long uid, String name) {
    CalendarSet.CalendarSetBuilder builder = CalendarSet.builder();

    if (StringUtil.isBlank(name)) {
      int i = 0;
      boolean isExist;
      do {
        name = CALENDAR_SET_DEFAULT_NAME + (i != 0 ? " (" + i + ")" : "");
        isExist =
            this.calendarSetDao.exists(
                PropertyFilter.newFilter().equal("owner.id", uid).equal("name", name));
        i++;
      } while (isExist);
      builder.name(name);
    }

    Calendar defaultCalendar = this.defaultCalendar(uid);

    builder
        .defaultCalendar(defaultCalendar)
        .index(ObjectUtil.defaultValue(this.calendarSetDao.getMaxIndex(uid), () -> 0) + 1)
        .owner(User.builder().id(uid).build());
    return this.calendarSetDao.save(builder.build());
  }

  private Calendar defaultCalendar(Long uid) {
    return this.calendarDao.defaultCalendar(uid);
  }

  public CalendarSet updateCalendarSet(Long id, CalendarSet calendarSet, Boolean merge) {
    calendarSet.setId(id);
    Integer index = calendarSet.getIndex();

    if (index != null) {
      CalendarSet source = this.calendarSetDao.getReferenceById(id);
      Integer sourceIndex = source.getIndex();
      List<CalendarSet> calendarSets =
          this.calendarSetDao.findAll(
              PropertyFilter.newFilter().equal("owner.id", source.getOwner().getId()),
              Sort.by("index").ascending());
      if (calendarSets.isEmpty()) {
        calendarSet.setIndex(1);
      } else {
        int maxIndex = calendarSets.get(calendarSets.size() - 1).getIndex();
        index = Math.max(Math.min(maxIndex, index), 0);
        int startIndex = Math.min(sourceIndex, index);
        int endIndex = Math.max(sourceIndex, index);
        List<CalendarSet> siblings =
            calendarSets.subList(startIndex - 1, endIndex).stream()
                .filter(item -> !item.getId().equals(id))
                .collect(Collectors.toList());
        rearrange(siblings, startIndex, endIndex, sourceIndex > index);
        calendarSet.setIndex(index);
      }
    }

    return this.calendarSetDao.update(calendarSet, merge);
  }

  public Boolean deleteCalendarSet(Long id) {
    CalendarSet source = this.calendarSetDao.getReferenceById(id);
    Integer sourceIndex = source.getIndex();

    List<CalendarSet> calendarSets =
        this.calendarSetDao.findAll(
            PropertyFilter.newFilter().equal("owner.id", source.getOwner().getId()),
            Sort.by("index").ascending());

    rearrange(calendarSets, sourceIndex, calendarSets.size(), false);

    this.calendarSetDao.delete(source);

    return true;
  }

  /**
   * 重新排序
   *
   * @param calendarSets 兄弟节点（不包含当前节点）
   * @param startIndex 开始位置
   * @param endIndex 结束位置
   * @param back true ? index + 1 : index -1;
   */
  public void rearrange(
      List<CalendarSet> calendarSets, int startIndex, int endIndex, boolean back) {
    List<CalendarSet> neighbors =
        calendarSets.stream()
            .filter(item -> item.getIndex() >= startIndex && item.getIndex() <= endIndex)
            .collect(Collectors.toList());
    for (CalendarSet item : neighbors) {
      item.setIndex(item.getIndex() + (back ? 1 : -1));
    }
    this.calendarSetDao.updateAllInBatch(neighbors);
  }

  /**
   * 重新排序
   *
   * @param calendars 兄弟节点（不包含当前节点）
   * @param startIndex 开始位置
   * @param endIndex 结束位置
   * @param back true ? index + 1 : index -1;
   */
  public void rearrangeCalendars(
      List<Calendar> calendars, int startIndex, int endIndex, boolean back) {
    List<Calendar> neighbors =
        calendars.stream()
            .filter(item -> item.getIndex() >= startIndex && item.getIndex() <= endIndex)
            .collect(Collectors.toList());
    for (Calendar item : neighbors) {
      item.setIndex(item.getIndex() + (back ? 1 : -1));
    }
    this.calendarDao.updateAllInBatch(neighbors);
  }

  public boolean verifyAccount(Long account, LoginUser user) {
    return this.calendarAccountDao.exists(
        PropertyFilter.newFilter().equal("id", account).equal("owner.id", user.getUid()));
  }

  public CalendarSet addCalendarToSet(Long id, Long set) {
    CalendarSet calendarSet = this.calendarSetDao.getReferenceById(set);
    Calendar calendar = this.calendarDao.getReferenceById(id);
    if (!ObjectUtil.exists(calendarSet.getCalendars(), "id", id)) {
      calendarSet.getCalendars().add(calendar);
    }
    return this.calendarSetDao.update(calendarSet);
  }

  public CalendarSet removeCalendarFromSet(Long id, Long set) {
    CalendarSet calendarSet = this.calendarSetDao.getReferenceById(set);
    if (!ObjectUtil.exists(calendarSet.getCalendars(), "id", id)) {
      return calendarSet;
    }
    ObjectUtil.remove(calendarSet.getCalendars(), "id", id);
    return this.calendarSetDao.update(calendarSet);
  }

  public CalendarEvent addCalendarEvent(Long calendar, CalendarEvent event) {
    event.setCalendar(this.calendarDao.getReferenceById(calendar));
    if (event.getRemind() == null) {
      event.setRemind(Remind.builder().alert(Alert.NONE).build());
    }
    if (event.getRepeat() == null) {
      event.setRepeat(Repeat.builder().repeat(RepeatType.NONE).build());
    }
    return this.calendarEventDao.save(event);
  }
}
