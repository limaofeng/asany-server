package cn.asany.ui.resources.graphql;

import cn.asany.ui.resources.bean.Icon;
import cn.asany.ui.resources.converter.IconConverter;
import cn.asany.ui.resources.graphql.input.IconCreateInput;
import cn.asany.ui.resources.graphql.input.IconUpdateInput;
import cn.asany.ui.resources.service.IconService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

@Component
public class IconGraphQLMutationResolver implements GraphQLMutationResolver {

    private final IconService iconService;
    private final IconConverter iconConverter;

    public IconGraphQLMutationResolver(IconService iconService, IconConverter iconConverter) {
        this.iconService = iconService;
        this.iconConverter = iconConverter;
    }

    public Icon createIcon(IconCreateInput input) {
        Long libraryId = input.getLibraryId();
        Icon icon = iconConverter.toIcon(input);
        return iconService.save(libraryId, icon);
    }

    public Icon updateIcon(Long id, IconUpdateInput input) {
        Long libraryId = input.getLibraryId();
        Icon icon = iconConverter.toIcon(input);
        icon.setId(id);
        return iconService.save(libraryId, icon);
    }

    public Boolean deleteIcon(Long id) {
        iconService.delete(id);
        return Boolean.TRUE;
    }

}
