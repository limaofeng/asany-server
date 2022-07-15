package cn.asany.ui.library.graphql.input;

import cn.asany.ui.library.domain.Library;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class ComponentLibraryFilter extends QueryFilter<ComponentLibraryFilter, Library> {}
