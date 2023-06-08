package cn.asany.ui.library.graphql.input;

import cn.asany.ui.library.domain.Library;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

@Data
@EqualsAndHashCode(callSuper = true)
public class LibraryFilter extends WhereInput<LibraryFilter, Library> {}
