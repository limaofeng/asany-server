package cn.asany.sunrise.calendar.convert;

import cn.asany.sunrise.calendar.domain.Calendar;
import cn.asany.sunrise.calendar.domain.CalendarEvent;
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

  @Mappings({
    @Mapping(target = "datetime.allDay", source = "allDay"),
    @Mapping(target = "datetime.starts", source = "starts"),
    @Mapping(target = "datetime.ends", source = "ends")
  })
  CalendarEvent toCalendarEvent(CalendarEventCreateInput input);
}
