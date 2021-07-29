package cn.asany.ui.library.graphql;

import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.converter.LibraryConverter;
import cn.asany.ui.library.graphql.input.IconInput;
import cn.asany.ui.library.graphql.input.LibraryCreateInput;
import cn.asany.ui.library.graphql.input.LibraryUpdateInput;
import cn.asany.ui.library.graphql.type.ILibrary;
import cn.asany.ui.library.service.LibraryService;
import cn.asany.ui.resources.bean.Icon;
import cn.asany.ui.resources.service.IconService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LibraryGraphQLMutationResolver implements GraphQLMutationResolver {

    private final IconService iconService;
    private final LibraryConverter libraryConverter;

    @Autowired
    private LibraryService libraryService;

    private final LibraryGraphQLQueryResolver queryResolver;

    public LibraryGraphQLMutationResolver(LibraryConverter libraryConverter, IconService iconService, LibraryGraphQLQueryResolver queryResolver) {
        this.iconService = iconService;
        this.libraryConverter = libraryConverter;
        this.queryResolver = queryResolver;
    }

    public List<Icon> importIcons(Long library, List<IconInput> icons) {
        return this.iconService.importIcons(library, icons.stream().map(libraryConverter::toIcon).collect(Collectors.toList()));
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
