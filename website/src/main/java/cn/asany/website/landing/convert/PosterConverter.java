package cn.asany.website.landing.convert;

import cn.asany.website.landing.bean.LandingPoster;
import cn.asany.website.landing.graphql.input.LandingPosterCreateInput;
import cn.asany.website.landing.graphql.input.LandingPosterUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PosterConverter {

  @Mappings({})
  LandingPoster toPoster(LandingPosterCreateInput input);

  @Mappings({})
  LandingPoster toPoster(LandingPosterUpdateInput input);
}
