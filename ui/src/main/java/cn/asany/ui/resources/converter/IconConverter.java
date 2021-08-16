package cn.asany.ui.resources.converter;

import cn.asany.ui.resources.bean.Icon;
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
