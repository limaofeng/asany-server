package cn.asany.system.convert;

import cn.asany.system.domain.ShortLink;
import cn.asany.system.graphql.input.ShortLinkCreateInput;
import java.util.List;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ShortLinkConverter {
  @IterableMapping(elementTargetType = ShortLink.class)
  List<ShortLink> toShortLinks(List<ShortLinkCreateInput> links);

  @Mappings({})
  ShortLink toShortLink(ShortLinkCreateInput input);
}
