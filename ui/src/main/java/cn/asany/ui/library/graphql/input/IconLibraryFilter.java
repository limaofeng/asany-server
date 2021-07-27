package cn.asany.ui.library.graphql.input;

import cn.asany.ui.library.bean.Library;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class IconLibraryFilter extends QueryFilter<IconLibraryFilter, Library> {
}
