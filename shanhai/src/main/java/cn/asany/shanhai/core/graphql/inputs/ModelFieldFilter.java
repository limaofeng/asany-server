package cn.asany.shanhai.core.graphql.inputs;

import cn.asany.shanhai.core.domain.ModelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

@Data
@EqualsAndHashCode(callSuper = false)
public class ModelFieldFilter extends QueryFilter<ModelFieldFilter, ModelField> {}
