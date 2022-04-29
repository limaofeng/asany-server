package cn.asany.website.landing.convert;

import cn.asany.website.landing.bean.LandingPage;
import cn.asany.website.landing.bean.LandingPoster;
import cn.asany.website.landing.bean.LandingStore;
import cn.asany.website.landing.graphql.input.LandingPageCreateInput;
import cn.asany.website.landing.graphql.input.LandingPageUpdateInput;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PageConverter {

  @Mappings({
    @Mapping(source = "poster", target = "poster", qualifiedByName = "toPosterId"),
    @Mapping(source = "stores", target = "stores", qualifiedByName = "toStoresFromIds"),
  })
  LandingPage toPage(LandingPageCreateInput input);

  @Mappings({
    @Mapping(source = "poster", target = "poster", qualifiedByName = "toPosterId"),
    @Mapping(source = "stores", target = "stores", qualifiedByName = "toStoresFromIds"),
  })
  LandingPage toPage(LandingPageUpdateInput input);

  @Named("toPosterId")
  default LandingPoster toPosterId(Long id) {
    if (id == null) {
      return null;
    }
    return LandingPoster.builder().id(id).build();
  }

  @Named("toStoresFromIds")
  default List<LandingStore> toStoresFromIds(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return new ArrayList<>();
    }
    return ids.stream()
        .map(id -> LandingStore.builder().id(id).build())
        .collect(Collectors.toList());
  }
}
