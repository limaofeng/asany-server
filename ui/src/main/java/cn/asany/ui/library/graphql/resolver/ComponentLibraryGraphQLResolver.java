package cn.asany.ui.library.graphql.resolver;

import cn.asany.ui.library.graphql.type.ComponentLibrary;
import cn.asany.ui.library.service.LibraryService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ComponentLibraryGraphQLResolver implements GraphQLResolver<ComponentLibrary> {

  private final LibraryService libraryService;

  public ComponentLibraryGraphQLResolver(LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  public List<cn.asany.ui.resources.domain.Component> components(ComponentLibrary library) {
    return library.getComponents();
  }

  /** 库内项目数 */
  public Long total(ComponentLibrary library) {
    return libraryService.getResourceTotal(library.getId());
  }
}
