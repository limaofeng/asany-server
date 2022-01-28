package cn.asany.sunrise.calendar.provider;

import cn.asany.sunrise.calendar.bean.Calendar;
import cn.asany.sunrise.calendar.bean.enums.CalendarProviderType;
import java.util.List;

public interface CalendarProvider {

  CalendarProviderType type();

  String getName();

  List<Calendar> loadCalendars();
}
