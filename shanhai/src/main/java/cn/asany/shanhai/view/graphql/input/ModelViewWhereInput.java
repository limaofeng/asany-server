package cn.asany.shanhai.view.graphql.input;

import cn.asany.shanhai.view.domain.ModelView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

@Data
@EqualsAndHashCode(callSuper = false)
public class ModelViewWhereInput extends WhereInput<ModelViewWhereInput, ModelView> {}
