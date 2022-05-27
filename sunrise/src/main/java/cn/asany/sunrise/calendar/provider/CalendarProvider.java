package cn.asany.sunrise.calendar.provider;

import cn.asany.sunrise.calendar.domain.Calendar;
import cn.asany.sunrise.calendar.domain.enums.CalendarProviderType;
import java.util.List;

public interface CalendarProvider {

  CalendarProviderType type();

  String getName();

  List<Calendar> loadCalendars();
}
