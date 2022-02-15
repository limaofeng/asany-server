package cn.asany.sunrise.calendar.convert;

import cn.asany.sunrise.calendar.bean.Calendar;
import cn.asany.sunrise.calendar.bean.CalendarEvent;
import cn.asany.sunrise.calendar.graphql.input.CalendarEventCreateInput;
import cn.asany.sunrise.calendar.graphql.input.CalendarUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CalendarConverter {

  @Mappings({})
  Calendar toCalendar(CalendarUpdateInput input);

  @Mappings({})
  CalendarEvent toCalendarEvent(CalendarEventCreateInput input);
}
