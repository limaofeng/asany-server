package cn.asany.website.data.convert;

import cn.asany.website.data.domain.Website;
import cn.asany.website.data.graphql.input.WebsiteCreateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface WebsiteConverter {

  @Mappings({})
  Website toWebsite(WebsiteCreateInput input);
}
