package cn.asany.ui.library.converter;

import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.bean.LibraryItem;
import cn.asany.ui.library.graphql.input.IconInput;
import cn.asany.ui.library.graphql.type.IconLibrary;
import cn.asany.ui.resources.bean.Icon;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", builder = @Builder, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface LibraryConverter {

    @IterableMapping(elementTargetType = IconLibrary.class)
    List<IconLibrary> toIconLibraries(List<Library> libraries);

    @Mappings({
        @Mapping(source = "items", target = "icons", qualifiedByName = "toIcons")
    })
    IconLibrary toIconLibrary(Library libraries);

    default List<Icon> toIcons(List<LibraryItem> items) {
        return items.stream().map(item -> {
            Icon icon = item.getResource(Icon.class);
            icon.setTags(item.getTags());
            return icon;
        }).collect(Collectors.toList());
    }

    default List<Icon> toIcons(Set<LibraryItem> items) {
        return items.stream().map(item -> {
            Icon icon = item.getResource(Icon.class);
            icon.setTags(item.getTags());
            return icon;
        }).collect(Collectors.toList());
    }

    Icon toIcon(IconInput input);
}
