package cn.asany.sunrise.calendar.convert;

import cn.asany.sunrise.calendar.bean.CalendarSet;
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

  @Mappings({})
  CalendarSet toCalendarSet(CalendarSetUpdateInput input);
}
