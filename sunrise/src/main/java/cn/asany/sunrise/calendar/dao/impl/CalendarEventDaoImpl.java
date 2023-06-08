package cn.asany.sunrise.calendar.dao.impl;

import cn.asany.sunrise.calendar.dao.CalendarEventDao;
import cn.asany.sunrise.calendar.domain.CalendarEvent;
import cn.asany.sunrise.calendar.domain.toys.CalendarEventDateStat;
import cn.asany.sunrise.calendar.domain.toys.DateRange;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.query.internal.NativeQueryImpl;
import org.jfantasy.framework.dao.hibernate.AliasToBeanResultTransformer;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Sort;

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
  private static final String QUERY_EVENT_DATE_STAT_BY_CALENDAR_SET_SQL =
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

  private static final String QUERY_EVENT_DATE_LIMIT_SQL =
      "    SELECT\n"
          + "    d.date,\n"
          + "    count( 1 ) AS number\n"
          + "    FROM\n"
          + "    sunrise_calendar_event_date d\n"
          + "    LEFT JOIN sunrise_calendar_event e ON e.id = d.event_id\n"
          + "    LEFT JOIN sunrise_calendar_set_items si ON e.calendar_id = si.calendar_id\n"
          + "    WHERE si.set_id = :calendarSet AND  d.date %s :date\n"
          + "    GROUP BY\n"
          + "    d.date\n"
          + "    ORDER BY\n"
          + "    d.date %s ";

  private static final String QUERY_EVENT_DATE_LIMIT_BY_UID_SQL =
      "SELECT\n"
          + "\td.date,\n"
          + "\tcount( 1 ) AS number \n"
          + "FROM\n"
          + "\tsunrise_calendar_event_date d\n"
          + "\tLEFT JOIN sunrise_calendar_event e ON e.id = d.event_id\n"
          + "\tLEFT JOIN sunrise_calendar cal ON e.calendar_id = cal.id\n"
          + "\tLEFT JOIN sunrise_calendar_account account ON account.id = cal.calendar_account \n"
          + "WHERE account.owner_id = :uid AND  d.date %s :date\n"
          + "    GROUP BY\n"
          + "    d.date\n"
          + "    ORDER BY\n"
          + "    d.date %s ";

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
    Query query = this.em.createNativeQuery(QUERY_EVENT_DATE_STAT_BY_CALENDAR_SET_SQL);
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

  @Override
  public DateRange calendarEventDateStartAndEndByUid(Long uid, Date date, int day) {
    // 查询结束时间
    String sql = String.format(QUERY_EVENT_DATE_LIMIT_BY_UID_SQL, ">", "asc");

    Query query = this.em.createNativeQuery("SELECT count(1) FROM (" + sql + ") as temp");
    query.setParameter("date", date);
    query.setParameter("uid", uid);
    long maxCount = Long.parseLong(query.getSingleResult().toString());

    query =
        this.em.createNativeQuery(sql + "LIMIT " + Math.min(day, Math.max(maxCount - 1, 0)) + ",1");
    query.setParameter("date", date);
    query.setParameter("uid", uid);
    query
        .unwrap(NativeQueryImpl.class)
        .setResultTransformer(new AliasToBeanResultTransformer(CalendarEventDateStat.class));
    List<CalendarEventDateStat> list = query.getResultList();
    Date end = list.isEmpty() ? date : list.get(0).getDate();

    // 查询开始时间
    sql = String.format(QUERY_EVENT_DATE_LIMIT_BY_UID_SQL, "<", "desc");

    query = this.em.createNativeQuery("SELECT count(1) FROM (" + sql + ") as temp");
    query.setParameter("date", date);
    query.setParameter("uid", uid);
    maxCount = Long.parseLong(query.getSingleResult().toString());

    query =
        this.em.createNativeQuery(sql + "LIMIT " + Math.min(day, Math.max(maxCount - 1, 0)) + ",1");
    query.setParameter("date", date);
    query.setParameter("uid", uid);
    query
        .unwrap(NativeQueryImpl.class)
        .setResultTransformer(new AliasToBeanResultTransformer(CalendarEventDateStat.class));
    list = query.getResultList();
    Date start = list.isEmpty() ? date : list.get(0).getDate();
    return DateRange.builder().start(start).end(end).build();
  }

  @Override
  public DateRange calendarEventDateStartAndEndByCalendarSet(Long calendarSet, Date date, int day) {
    // 查询结束时间
    String sql = String.format(QUERY_EVENT_DATE_LIMIT_SQL, ">", "asc");

    Query query = this.em.createNativeQuery("SELECT count(1) FROM (" + sql + ") as temp");
    query.setParameter("date", date);
    query.setParameter("calendarSet", calendarSet);
    long maxCount = Long.parseLong(query.getSingleResult().toString());

    query =
        this.em.createNativeQuery(sql + "LIMIT " + Math.min(day, Math.max(maxCount - 1, 0)) + ",1");
    query.setParameter("date", date);
    query.setParameter("calendarSet", calendarSet);
    query
        .unwrap(NativeQueryImpl.class)
        .setResultTransformer(new AliasToBeanResultTransformer(CalendarEventDateStat.class));
    List<CalendarEventDateStat> list = query.getResultList();
    Date end = list.isEmpty() ? date : list.get(0).getDate();

    // 查询开始时间
    sql = String.format(QUERY_EVENT_DATE_LIMIT_SQL, "<", "desc");

    query = this.em.createNativeQuery("SELECT count(1) FROM (" + sql + ") as temp");
    query.setParameter("date", date);
    query.setParameter("calendarSet", calendarSet);
    maxCount = Long.parseLong(query.getSingleResult().toString());

    query =
        this.em.createNativeQuery(sql + "LIMIT " + Math.min(day, Math.max(maxCount - 1, 0)) + ",1");
    query.setParameter("date", date);
    query.setParameter("calendarSet", calendarSet);
    query
        .unwrap(NativeQueryImpl.class)
        .setResultTransformer(new AliasToBeanResultTransformer(CalendarEventDateStat.class));
    list = query.getResultList();
    Date start = list.isEmpty() ? date : list.get(0).getDate();
    return DateRange.builder().start(start).end(end).build();
  }

  @Override
  public List<CalendarEvent> findAllWithDates(PropertyFilter filter, Sort orderBy) {
    return this.findAll(filter, orderBy);
  }
}
