package cn.asany.ui.library.graphql;

import cn.asany.ui.library.convert.LibraryConverter;
import cn.asany.ui.library.domain.Library;
import cn.asany.ui.library.graphql.input.IconInput;
import cn.asany.ui.library.graphql.input.LibraryCreateInput;
import cn.asany.ui.library.graphql.input.LibraryUpdateInput;
import cn.asany.ui.library.graphql.type.ILibrary;
import cn.asany.ui.library.service.LibraryService;
import cn.asany.ui.resources.domain.Icon;
import cn.asany.ui.resources.service.IconService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class LibraryGraphQLMutationResolver implements GraphQLMutationResolver {

  private final IconService iconService;
  private final LibraryConverter libraryConverter;
  private final LibraryService libraryService;

  private final LibraryGraphQLQueryResolver queryResolver;

  public LibraryGraphQLMutationResolver(
      LibraryConverter libraryConverter,
      IconService iconService,
      LibraryGraphQLQueryResolver queryResolver,
      LibraryService libraryService) {
    this.iconService = iconService;
    this.libraryConverter = libraryConverter;
    this.queryResolver = queryResolver;
    this.libraryService = libraryService;
  }

  public Set<Icon> importIcons(Long library, List<IconInput> icons) {
    return this.iconService.importIcons(
        library, icons.stream().map(libraryConverter::toIcon).collect(Collectors.toList()));
  }

  public ILibrary createLibrary(LibraryCreateInput input) {
    Library library = libraryConverter.toLibrary(input);
    return libraryConverter.toLibrary(libraryService.save(library));
  }

  public ILibrary updateLibrary(Long id, LibraryUpdateInput input) {
    Library library = libraryConverter.toLibrary(input);
    return libraryConverter.toLibrary(libraryService.update(id, library));
  }

  public Boolean deleteLibrary(Long id) {
    libraryService.delete(id);
    return Boolean.TRUE;
  }
}
