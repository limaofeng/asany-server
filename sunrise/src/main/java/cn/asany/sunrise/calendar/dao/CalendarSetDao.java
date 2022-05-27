package cn.asany.sunrise.calendar.dao;

import cn.asany.sunrise.calendar.domain.CalendarSet;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 日历集
 *
 * @author limaofeng
 */
@Repository
public interface CalendarSetDao extends JpaRepository<CalendarSet, Long> {
  @Query(value = "SELECT max(index) FROM CalendarSet where owner.id = :uid")
  Integer getMaxIndex(@Param("uid") Long uid);
}
