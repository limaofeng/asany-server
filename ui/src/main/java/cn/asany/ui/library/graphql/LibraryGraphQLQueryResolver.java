package cn.asany.ui.library.graphql;

import cn.asany.base.common.graphql.input.OwnershipInput;
import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.bean.enums.LibraryType;
import cn.asany.ui.library.converter.LibraryConverter;
import cn.asany.ui.library.graphql.type.ILibrary;
import cn.asany.ui.library.graphql.type.IconLibrary;
import cn.asany.ui.library.service.LibraryService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class LibraryGraphQLQueryResolver implements GraphQLQueryResolver {

    private final LibraryService libraryService;
    private final LibraryConverter libraryConverter;

    public LibraryGraphQLQueryResolver(LibraryService libraryService, LibraryConverter libraryConverter) {
        this.libraryService = libraryService;
        this.libraryConverter = libraryConverter;
    }

    public List<ILibrary> libraries(LibraryType type, OwnershipInput ownership) {
        if (type == LibraryType.ICONS) {
            return new ArrayList<>(iconLibraries(ownership));
        }
        return new ArrayList<>();
    }

    public IconLibrary iconLibrary(Long id) {
        Optional<Library> library = libraryService.findByIdWithIcon(id);
        return library.map(this.libraryConverter::toIconLibrary).orElse(null);
    }

    public List<IconLibrary> iconLibraries(OwnershipInput ownership) {
        List<Library> libraries = libraryService.libraries(LibraryType.ICONS);
        return libraryConverter.toIconLibraries(libraries);
    }

//    public List<Icon> icons() {
//        List<Icon> libraries = libraryService.icons(LibraryType.ICONS);
//        return
//    }

}
