package cn.asany.ui.library.graphql;

import cn.asany.base.common.graphql.input.OwnershipInput;
import cn.asany.ui.library.convert.LibraryConverter;
import cn.asany.ui.library.domain.Library;
import cn.asany.ui.library.domain.enums.LibraryType;
import cn.asany.ui.library.graphql.input.IconLibraryFilter;
import cn.asany.ui.library.graphql.type.ILibrary;
import cn.asany.ui.library.graphql.type.IconLibrary;
import cn.asany.ui.library.service.LibraryService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Component;

@Component
public class LibraryGraphQLQueryResolver implements GraphQLQueryResolver {

  private final LibraryService libraryService;
  private final LibraryConverter libraryConverter;

  public LibraryGraphQLQueryResolver(
      LibraryService libraryService, LibraryConverter libraryConverter) {
    this.libraryService = libraryService;
    this.libraryConverter = libraryConverter;
  }

  public List<ILibrary> libraries(
      LibraryType type, OwnershipInput ownership, DataFetchingEnvironment environment) {
    if (type == LibraryType.ICONS) {
      return new ArrayList<>(iconLibraries(new IconLibraryFilter(), ownership, environment));
    }
    return new ArrayList<>();
  }

  public IconLibrary iconLibrary(Long id) {
    Optional<Library> library = libraryService.findByIdWithIcon(id);
    return library.map(this.libraryConverter::toIconLibrary).orElse(null);
  }

  public List<IconLibrary> iconLibraries(
      IconLibraryFilter filter, OwnershipInput ownership, DataFetchingEnvironment environment) {
    filter = ObjectUtil.defaultValue(filter, () -> new IconLibraryFilter());
    boolean with = environment.getSelectionSet().contains("icons");
    List<Library> libraries =
        libraryService.libraries(filter.getBuilder(), LibraryType.ICONS, with);
    if (!with) {
      return libraryConverter.toLibraries(libraries);
    }
    return libraryConverter.toIconLibraries(libraries);
  }
}
