package cn.asany.sunrise.calendar.dao;

import cn.asany.sunrise.calendar.bean.CalendarSet;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 日历集
 *
 * @author limaofeng
 */
@Repository
public interface CalendarSetDao extends JpaRepository<CalendarSet, Long> {}
