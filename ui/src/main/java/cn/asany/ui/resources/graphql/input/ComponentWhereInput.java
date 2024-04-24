package cn.asany.ui.resources.graphql.input;

import cn.asany.ui.resources.domain.Component;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.inputs.WhereInput;

/**
 * 模型筛选
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ComponentWhereInput extends WhereInput<ComponentWhereInput, Component> {}
