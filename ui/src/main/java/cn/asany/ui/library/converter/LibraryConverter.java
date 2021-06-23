package cn.asany.ui.library.converter;

import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.graphql.type.IconLibrary;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface LibraryConverter {

    @IterableMapping(elementTargetType = IconLibrary.class)
    List<IconLibrary> toIconLibraries(List<Library> libraries);

    IconLibrary toIconLibrary(Library libraries);

}
