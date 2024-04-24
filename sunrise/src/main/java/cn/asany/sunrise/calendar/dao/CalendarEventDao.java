package cn.asany.sunrise.calendar.dao;

import cn.asany.sunrise.calendar.domain.CalendarEvent;
import cn.asany.sunrise.calendar.domain.toys.CalendarEventDateStat;
import cn.asany.sunrise.calendar.domain.toys.DateRange;
import java.util.Date;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

/**
 * Calendar Event Dao
 *
 * @author limaofeng
 */
@Repository
public interface CalendarEventDao extends AnyJpaRepository<CalendarEvent, Long> {
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
   * @param uid 用户ID
   * @param date 指定日期
   * @param day 天数
   */
  DateRange calendarEventDateStartAndEndByUid(Long uid, Date date, int day);

  /**
   * 获取指定日期的前N及后N天的 开始及结束日期
   *
   * @param calendarSet 日历集
   * @param date 指定日期
   * @param day 天数
   */
  DateRange calendarEventDateStartAndEndByCalendarSet(Long calendarSet, Date date, int day);

  @EntityGraph(value = "Graph.CalendarEvent.FetchDates", type = EntityGraph.EntityGraphType.FETCH)
  List<CalendarEvent> findAllWithDates(PropertyFilter filter, Sort orderBy);
}
