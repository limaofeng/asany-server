package cn.asany.nuwa.app.converter;

import cn.asany.nuwa.app.domain.Application;
import cn.asany.nuwa.app.domain.ApplicationRoute;
import cn.asany.nuwa.app.graphql.input.ApplicationRouteCreateInput;
import cn.asany.nuwa.app.graphql.input.ApplicationRouteUpdateInput;
import cn.asany.ui.resources.domain.Component;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ApplicationRouteConverter {

  @Mappings({
    @Mapping(target = "application", source = "application", qualifiedByName = "toApplication"),
    @Mapping(target = "parent", source = "parentRoute", qualifiedByName = "toParentRoute"),
    @Mapping(target = "component", source = "component", qualifiedByName = "toComponent")
  })
  ApplicationRoute toRoute(ApplicationRouteCreateInput input);

  @Mappings({
    @Mapping(target = "parent", source = "parentRoute", qualifiedByName = "toParentRoute"),
    @Mapping(target = "component", source = "component", qualifiedByName = "toComponent"),
    @Mapping(target = "breadcrumb", source = "breadcrumb", qualifiedByName = "toComponent")
  })
  ApplicationRoute toRoute(ApplicationRouteUpdateInput input);

  @Named("toComponent")
  default Component toComponent(Long id) {
    if (id == null) {
      return null;
    }
    return Component.builder().id(id).build();
  }

  @Named("toApplication")
  default Application toApplication(Long id) {
    if (id == null) {
      return null;
    }
    return Application.builder().id(id).build();
  }

  @Named("toParentRoute")
  default ApplicationRoute toParentRoute(Long id) {
    if (id == null) {
      return null;
    }
    return ApplicationRoute.builder().id(id).build();
  }
}
