package cn.asany.shanhai.core.graphql.inputs;

import cn.asany.shanhai.core.domain.ModelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.inputs.WhereInput;

@Data
@EqualsAndHashCode(callSuper = false)
public class ModelFieldWhereInput extends WhereInput<ModelFieldWhereInput, ModelField> {}
