package cn.asany.ui.resources.graphql;

import cn.asany.ui.resources.bean.Icon;
import cn.asany.ui.resources.bean.spec.LibrarySpecification;
import cn.asany.ui.resources.converter.IconConverter;
import cn.asany.ui.resources.graphql.input.IconFilter;
import cn.asany.ui.resources.service.IconService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IconGraphQLQueryResolver implements GraphQLQueryResolver {

    private final IconService iconService;
    private final IconConverter iconConverter;

    public IconGraphQLQueryResolver(IconService iconService, IconConverter iconConverter) {
        this.iconService = iconService;
        this.iconConverter = iconConverter;
    }

    public List<Icon> icons(IconFilter filter) {
        filter = ObjectUtil.defaultValue(filter, IconFilter::new);
        PropertyFilterBuilder builder = filter.getBuilder();
        if (filter.getLibrary_in() != null) {
            builder.and(new LibrarySpecification(filter.getLibrary_in()));
        } else if (filter.getLibrary() != null) {
            builder.and(new LibrarySpecification(filter.getLibrary()));
        }
        return this.iconService.findAll(builder.build());
    }

}
