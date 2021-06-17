package cn.asany.nuwa.app.converter;

import cn.asany.nuwa.app.bean.ApplicationRoute;
import cn.asany.nuwa.app.bean.Routespace;
import cn.asany.nuwa.template.bean.ApplicationTemplateRoute;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ApplicationConverter {

    @IterableMapping(elementTargetType = ApplicationRoute.class)
    List<ApplicationRoute> toRoutes(List<ApplicationTemplateRoute> routes, @Context Routespace routespace);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "parent", ignore = true),
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdAt", ignore = true),
        @Mapping(target = "updatedBy", ignore = true),
        @Mapping(target = "updatedAt", ignore = true),
        @Mapping(target = "application", ignore = true),
        @Mapping(target = "space", source = "id", qualifiedByName = "routespace"),
    })
    ApplicationRoute toRoute(ApplicationTemplateRoute route, @Context Routespace routespace);

    @Named("routespace")
    default Routespace routespace(Long route, @Context Routespace routespace) {
        return routespace;
    }

}
