package cn.asany.sunrise.calendar.dao;

import cn.asany.sunrise.calendar.bean.Calendar;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Calendar Dao
 *
 * @author limaofeng
 */
@Repository
public interface CalendarDao extends JpaRepository<Calendar, Long> {}
