package cn.asany.ui.library.graphql.input;

import cn.asany.ui.library.domain.Library;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

/**
 * 图标库查询过滤
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IconLibraryWhereInput extends WhereInput<IconLibraryWhereInput, Library> {}
