package cn.asany.myjob.factory.convert;

import cn.asany.myjob.factory.domain.Screen;
import cn.asany.myjob.factory.graphql.input.ScreenCreateInput;
import cn.asany.myjob.factory.graphql.input.ScreenUpdateInput;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ScreenConverter {
  Screen toScreen(ScreenCreateInput input);

  Screen toScreen(ScreenUpdateInput input);
}
