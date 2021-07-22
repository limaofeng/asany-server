package cn.asany.ui.resources.graphql.input;

import cn.asany.ui.library.graphql.input.IconInput;
import lombok.Data;

@Data
public class IconCreateInput extends IconInput {

    private Long libraryId;

}
