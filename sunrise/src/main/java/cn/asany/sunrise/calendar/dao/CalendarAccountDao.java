package cn.asany.sunrise.calendar.dao;

import cn.asany.sunrise.calendar.domain.CalendarAccount;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/** 日历账号 */
@Repository
public interface CalendarAccountDao extends AnyJpaRepository<CalendarAccount, Long> {}
