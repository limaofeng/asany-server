package cn.asany.ui.resources.graphql.input;

import cn.asany.ui.library.graphql.input.IconInput;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class IconUpdateInput extends IconInput {
    private Long libraryId;
}
