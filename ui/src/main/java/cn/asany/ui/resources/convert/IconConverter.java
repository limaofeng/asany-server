package cn.asany.ui.resources.convert;

import cn.asany.ui.resources.domain.Icon;
import cn.asany.ui.resources.graphql.input.IconCreateInput;
import cn.asany.ui.resources.graphql.input.IconUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IconConverter {

  Icon toIcon(IconCreateInput input);

  Icon toIcon(IconUpdateInput input);
}
