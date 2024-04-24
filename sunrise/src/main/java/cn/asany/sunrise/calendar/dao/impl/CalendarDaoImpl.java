package cn.asany.sunrise.calendar.dao.impl;

import cn.asany.sunrise.calendar.dao.CalendarDao;
import cn.asany.sunrise.calendar.domain.Calendar;
import cn.asany.sunrise.calendar.domain.enums.CalendarType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.data.domain.Sort;

public class CalendarDaoImpl extends SimpleAnyJpaRepository<Calendar, Long> implements CalendarDao {

  public CalendarDaoImpl(EntityManager entityManager) {
    super(Calendar.class, entityManager);
  }

  @Override
  public Integer getMaxIndex(Long account) {
    Query query =
        em.createNativeQuery(
            "SELECT max(sort) FROM sunrise_calendar WHERE calendar_account = :account");
    query.setParameter("account", account);
    Object result = query.getSingleResult();
    return result == null ? 0 : Integer.parseInt(result.toString());
  }

  @Override
  public List<Calendar> findAllWithAccountByUid(Long uid) {
    List<Calendar> calendars =
        this.findAll(
            PropertyFilter.newFilter().equal("account.owner.id", uid),
            Sort.by("index").ascending());
    return ObjectUtil.sort(
        calendars,
        Comparator.comparing(
            l ->
                BigDecimal.valueOf(l.getAccount().getIndex())
                    .multiply(BigDecimal.valueOf(100000L))
                    .add(BigDecimal.valueOf(l.getIndex()))));
  }

  @Override
  public Calendar defaultCalendar(Long uid) {
    List<Calendar> calendars =
        this.findAll(
            PropertyFilter.newFilter()
                .equal("account.owner.id", uid)
                .equal("type", CalendarType.SUNRISE),
            1,
            Sort.by("index").ascending());
    return calendars.isEmpty() ? null : calendars.get(0);
  }
}
