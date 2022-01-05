package cn.asany.sunrise.calendar.dao;

import cn.asany.sunrise.calendar.bean.CalendarEvent;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
  @Query("DELETE FROM CalendarEvent event WHERE event.calendar.id = :calendarId")
  void deleteEventsByCalendarId(@Param("calendarId") Long calendarId);
}
