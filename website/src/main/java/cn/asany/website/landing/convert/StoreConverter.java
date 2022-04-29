package cn.asany.website.landing.convert;

import cn.asany.base.common.bean.Geolocation;
import cn.asany.website.landing.bean.LandingStore;
import cn.asany.website.landing.graphql.input.LandingStoreCreateInput;
import cn.asany.website.landing.graphql.input.LandingStoreUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface StoreConverter {

  @Mappings({
    @Mapping(source = "location", target = "location", qualifiedByName = "toLocation"),
  })
  LandingStore toStore(LandingStoreCreateInput input);

  @Mappings({
    @Mapping(source = "location", target = "location", qualifiedByName = "toLocation"),
  })
  LandingStore toStore(LandingStoreUpdateInput input);

  @Named("toLocation")
  default Geolocation toLocation(Geolocation location) {
    if (location.getLatitude() == null && location.getLongitude() == null) {
      return null;
    }
    return location;
  }
}
