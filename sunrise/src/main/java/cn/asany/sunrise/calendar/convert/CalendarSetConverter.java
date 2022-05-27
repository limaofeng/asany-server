package cn.asany.sunrise.calendar.convert;

import cn.asany.sunrise.calendar.domain.CalendarSet;
import cn.asany.sunrise.calendar.graphql.input.CalendarSetCreateInput;
import cn.asany.sunrise.calendar.graphql.input.CalendarSetUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CalendarSetConverter {

  @Mappings({})
  CalendarSet toCalendarSet(CalendarSetCreateInput input);

  @Mappings({
    @Mapping(target = "defaultCalendar.id", source = "defaultCalendar"),
  })
  CalendarSet toCalendarSet(CalendarSetUpdateInput input);
}
