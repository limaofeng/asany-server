package cn.asany.ui.resources.graphql.input;

import cn.asany.ui.resources.domain.Component;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * 模型筛选
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ComponentFilter extends QueryFilter<ComponentFilter, Component> {}
