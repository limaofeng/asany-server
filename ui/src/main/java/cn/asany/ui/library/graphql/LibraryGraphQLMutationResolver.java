package cn.asany.ui.library.graphql;

import cn.asany.ui.library.converter.LibraryConverter;
import cn.asany.ui.library.graphql.input.IconInput;
import cn.asany.ui.library.graphql.type.IconLibrary;
import cn.asany.ui.library.service.IconService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LibraryGraphQLMutationResolver implements GraphQLMutationResolver {

    private final IconService iconService;
    private final LibraryConverter libraryConverter;

    private final LibraryGraphQLQueryResolver queryResolver;

    public LibraryGraphQLMutationResolver(LibraryConverter libraryConverter, IconService iconService, LibraryGraphQLQueryResolver queryResolver) {
        this.iconService = iconService;
        this.libraryConverter = libraryConverter;
        this.queryResolver = queryResolver;
    }

    public IconLibrary importIcons(Long library, List<IconInput> icons) {
        this.iconService.importIcons(library, icons.stream().map(item -> libraryConverter.toIcon(item)).collect(Collectors.toList()));
        return queryResolver.iconLibrary(library);
    }

}
