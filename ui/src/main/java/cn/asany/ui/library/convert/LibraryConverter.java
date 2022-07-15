package cn.asany.ui.library.convert;

import cn.asany.ui.library.domain.Library;
import cn.asany.ui.library.domain.LibraryItem;
import cn.asany.ui.library.domain.enums.LibraryType;
import cn.asany.ui.library.graphql.input.IconInput;
import cn.asany.ui.library.graphql.input.LibraryCreateInput;
import cn.asany.ui.library.graphql.input.LibraryUpdateInput;
import cn.asany.ui.library.graphql.type.ComponentLibrary;
import cn.asany.ui.library.graphql.type.ILibrary;
import cn.asany.ui.library.graphql.type.IconLibrary;
import cn.asany.ui.resources.domain.Component;
import cn.asany.ui.resources.domain.Icon;
import java.util.*;
import java.util.stream.Collectors;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface LibraryConverter {

  default List<IconLibrary> toLibraries(List<Library> libraries) {
    List<IconLibrary> iconLibraries = new ArrayList<>();
    for (Library library : libraries) {
      iconLibraries.add(toIconSimplifyLibrary(library));
    }
    return iconLibraries;
  }

  @Mappings(value = @Mapping(target = "icons", ignore = true))
  IconLibrary toIconSimplifyLibrary(Library libraries);

  default ILibrary toLibrary(Library library) {
    if (library.getType() == LibraryType.ICONS) {
      return toIconLibrary(library);
    }
    return null;
  }

  default List<IconLibrary> toIconLibraries(List<Library> libraries) {
    List<IconLibrary> iconLibraries = new ArrayList<>();
    for (Library library : libraries) {
      iconLibraries.add(toIconLibrary(library));
    }
    return iconLibraries;
  }

  @Mappings(value = @Mapping(source = "items", target = "icons", qualifiedByName = "toIcons"))
  IconLibrary toIconLibrary(Library libraries);

  @Named("toIcons")
  default Set<Icon> toIcons(Set<LibraryItem> items) {
    if (items == null) {
      return Collections.emptySet();
    }
    return items.stream()
        .map(
            item -> {
              Icon icon = item.getResource(Icon.class);
              icon.setTags(item.getTags());
              return icon;
            })
        .collect(Collectors.toSet());
  }

  @Mappings(
      value = @Mapping(source = "items", target = "components", qualifiedByName = "toComponents"))
  ComponentLibrary toComponentLibrary(Library library);

  @Named("toComponents")
  default List<Component> toComponents(Set<LibraryItem> items) {
    if (items == null) {
      return Collections.emptyList();
    }
    return ObjectUtil.sort(
        items.stream()
            .map(
                item -> {
                  Component component = item.getResource(Component.class);
                  component.setTags(item.getTags());
                  return component;
                })
            .collect(Collectors.toList()),
        "id",
        "desc");
  }

  Icon toIcon(IconInput input);

  Library toLibrary(LibraryCreateInput input);

  Library toLibrary(LibraryUpdateInput input);
}
