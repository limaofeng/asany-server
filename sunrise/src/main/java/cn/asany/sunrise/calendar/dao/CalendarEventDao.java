package cn.asany.sunrise.calendar.dao;

import cn.asany.sunrise.calendar.bean.CalendarEvent;
import cn.asany.sunrise.calendar.bean.toys.CalendarEventDateStat;
import cn.asany.sunrise.calendar.bean.toys.DateRange;
import java.util.Date;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

/**
 * Calendar Event Dao
 *
 * @author limaofeng
 */
@Repository
public interface CalendarEventDao extends JpaRepository<CalendarEvent, Long> {
  /**
   * 删除事件
   *
   * @param calendarId ID
   */
  @Modifying
  void deleteEventsByCalendarId(Long calendarId);

  /**
   * 日历集统计
   *
   * @param calendarSet 日历集
   * @param starts 开始时间
   * @param ends 结束时间
   * @return List<CalendarEventDateStat>
   */
  List<CalendarEventDateStat> calendarEventDatesByCalendarSet(
      Long calendarSet, Date starts, Date ends);

  /**
   * 日历集统计
   *
   * @param calendar 日历
   * @param starts 开始时间
   * @param ends 结束时间
   * @return List<CalendarEventDateStat>
   */
  List<CalendarEventDateStat> calendarEventDatesByCalendar(Long calendar, Date starts, Date ends);
  /**
   * 日历集统计
   *
   * @param uid 用户ID
   * @param starts 开始时间
   * @param ends 结束时间
   * @return List<CalendarEventDateStat>
   */
  List<CalendarEventDateStat> calendarEventDates(Long uid, Date starts, Date ends);

  /**
   * 获取指定日期的前N及后N天的 开始及结束日期
   *
   * @param calendarSet 日历集
   * @param date 指定日期
   * @param day 天数
   */
  DateRange calendarEventDateStartAndEndByCalendarSet(Long calendarSet, Date date, int day);
}
