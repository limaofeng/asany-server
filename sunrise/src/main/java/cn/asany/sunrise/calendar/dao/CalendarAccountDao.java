package cn.asany.sunrise.calendar.dao;

import cn.asany.sunrise.calendar.domain.CalendarAccount;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/** 日历账号 */
@Repository
public interface CalendarAccountDao extends JpaRepository<CalendarAccount, Long> {}
