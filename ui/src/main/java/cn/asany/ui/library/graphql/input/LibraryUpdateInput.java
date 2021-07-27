package cn.asany.ui.library.graphql.input;

import cn.asany.ui.library.bean.enums.LibraryType;
import lombok.Data;

@Data
public class LibraryUpdateInput {
    private LibraryType type;
    private String name;
    private String description;
}
