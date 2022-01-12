package cn.asany.sunrise.calendar.dao.impl;

import cn.asany.sunrise.calendar.bean.CalendarEvent;
import cn.asany.sunrise.calendar.bean.toys.CalendarEventDateStat;
import cn.asany.sunrise.calendar.dao.CalendarEventDao;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.query.internal.NativeQueryImpl;
import org.jfantasy.framework.dao.hibernate.AliasToBeanResultTransformer;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;

/**
 * CalendarEvent Dao Impl
 *
 * @author limaofeng
 */
public class CalendarEventDaoImpl extends ComplexJpaRepository<CalendarEvent, Long>
    implements CalendarEventDao {

  private static final String DELETE_EVENT_DATES_SQL =
      "DELETE d FROM sunrise_calendar_event_date d LEFT JOIN sunrise_calendar_event e ON e.id = d.event_id WHERE e.calendar_id = :calendarId";
  private static final String DELETE_EVENTS_SQL =
      "DELETE FROM sunrise_calendar_event e WHERE e.calendar_id = :calendarId";
  private static final String QUERY_EVENT_DATE_STAT_BY_CALENDARSET_SQL =
      "SELECT d.date, count(1) as number FROM sunrise_calendar_event_date d \n"
          + "LEFT JOIN sunrise_calendar_event e ON e.id = d.event_id \n"
          + "LEFT JOIN sunrise_calendar_set_items si ON e.calendar_id = si.calendar_id\n"
          + "WHERE si.set_id = :calendarSet AND d.date between :starts AND :ends \n"
          + "GROUP BY d.date ORDER BY d.date";
  private static final String QUERY_EVENT_DATE_STAT_BY_CALENDAR_SQL =
      "SELECT d.date, count(1) as number FROM sunrise_calendar_event_date d \n"
          + "LEFT JOIN sunrise_calendar_event e ON e.id = d.event_id \n"
          + "WHERE e.calendar_id = :calendar AND d.date between :starts AND :ends \n"
          + "GROUP BY d.date ORDER BY d.date";
  private static final String QUERY_EVENT_DATE_STAT_ALL_SQL =
      "SELECT d.date, count(1) as number FROM sunrise_calendar_event_date d \n"
          + "LEFT JOIN sunrise_calendar_event e ON e.id = d.event_id \n"
          + "WHERE d.date between :starts AND :ends \n"
          + "GROUP BY d.date ORDER BY d.date";

  public CalendarEventDaoImpl(EntityManager entityManager) {
    super(CalendarEvent.class, entityManager);
  }

  @Override
  public void deleteEventsByCalendarId(Long calendarId) {
    Query query = this.em.createNativeQuery(DELETE_EVENT_DATES_SQL);
    query.setParameter("calendarId", calendarId);
    query.executeUpdate();
    query = this.em.createNativeQuery(DELETE_EVENTS_SQL);
    query.setParameter("calendarId", calendarId);
    query.executeUpdate();
  }

  @Override
  public List<CalendarEventDateStat> calendarEventDatesByCalendarSet(
      Long calendarSet, Date starts, Date ends) {
    Query query = this.em.createNativeQuery(QUERY_EVENT_DATE_STAT_BY_CALENDARSET_SQL);
    query.setParameter("calendarSet", calendarSet);
    query.setParameter("starts", starts);
    query.setParameter("ends", ends);
    query
        .unwrap(NativeQueryImpl.class)
        .setResultTransformer(new AliasToBeanResultTransformer(CalendarEventDateStat.class));
    return query.getResultList();
  }

  @Override
  public List<CalendarEventDateStat> calendarEventDatesByCalendar(
      Long calendar, Date starts, Date ends) {
    Query query = this.em.createNativeQuery(QUERY_EVENT_DATE_STAT_BY_CALENDAR_SQL);
    query.setParameter("calendar", calendar);
    query.setParameter("starts", starts);
    query.setParameter("ends", ends);
    query
        .unwrap(NativeQueryImpl.class)
        .setResultTransformer(new AliasToBeanResultTransformer(CalendarEventDateStat.class));
    return query.getResultList();
  }

  @Override
  public List<CalendarEventDateStat> calendarEventDates(Long uid, Date starts, Date ends) {
    Query query = this.em.createNativeQuery(QUERY_EVENT_DATE_STAT_ALL_SQL);
    query.setParameter("starts", starts);
    query.setParameter("ends", ends);
    query
        .unwrap(NativeQueryImpl.class)
        .setResultTransformer(new AliasToBeanResultTransformer(CalendarEventDateStat.class));
    return query.getResultList();
  }
}
