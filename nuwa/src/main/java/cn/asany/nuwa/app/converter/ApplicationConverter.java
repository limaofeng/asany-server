package cn.asany.nuwa.app.converter;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.bean.ApplicationRoute;
import cn.asany.nuwa.app.bean.Routespace;
import cn.asany.nuwa.template.bean.ApplicationTemplate;
import cn.asany.nuwa.template.bean.ApplicationTemplateRoute;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ApplicationConverter {

    @IterableMapping(elementTargetType = ApplicationRoute.class)
    List<ApplicationRoute> toRoutes(List<ApplicationTemplateRoute> routes, @Context Application application, @Context Routespace routespace);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "parent", ignore = true),
        @Mapping(target = "application", qualifiedByName = "application"),
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdAt", ignore = true),
        @Mapping(target = "updatedBy", ignore = true),
        @Mapping(target = "updatedAt", ignore = true)
    })
    ApplicationRoute toRoute(ApplicationTemplateRoute route, @Context Application application, @Context Routespace routespace);

    @Named("application")
    default Application application(ApplicationTemplate applicationTemplate, @Context Application application) {
        return application;
    }

    @Named("routespace")
    default Routespace routespace(Long route, @Context Routespace routespace) {
        return routespace;
    }

}
