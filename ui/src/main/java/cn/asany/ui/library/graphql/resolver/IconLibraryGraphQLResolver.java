package cn.asany.ui.library.graphql.resolver;

import cn.asany.ui.library.graphql.type.IconLibrary;
import cn.asany.ui.library.service.LibraryService;
import cn.asany.ui.resources.bean.Icon;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IconLibraryGraphQLResolver implements GraphQLResolver<IconLibrary> {

  @Autowired private LibraryService libraryService;

  public List<Icon> icons(IconLibrary library) {
    return library.getIcons();
  }

  /** 库内项目数 */
  public Long total(IconLibrary library) {
    return libraryService.getResourceTotal(library.getId());
  }
}
