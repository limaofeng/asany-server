package cn.asany.sunrise.calendar.dao;

import cn.asany.sunrise.calendar.domain.Calendar;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Calendar Dao
 *
 * @author limaofeng
 */
@Repository
public interface CalendarDao extends JpaRepository<Calendar, Long> {

  Integer getMaxIndex(@Param("account") Long account);

  @EntityGraph(value = "Graph.Calendar.FetchAccount", type = EntityGraph.EntityGraphType.FETCH)
  List<Calendar> findAllWithAccountByUid(Long uid);

  Calendar defaultCalendar(Long uid);
}
