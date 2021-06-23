package cn.asany.ui.library.graphql.resolver;

import cn.asany.ui.library.graphql.type.IconLibrary;
import cn.asany.ui.resources.bean.Icon;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IconLibraryGraphQLResolver implements GraphQLResolver<IconLibrary> {

    public List<Icon> icons(IconLibrary library) {
        return new ArrayList<>();
    }

}
