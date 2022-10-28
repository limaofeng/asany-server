package cn.asany.shanhai.view.graphql.input;

import cn.asany.shanhai.view.domain.ModelView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

@Data
@EqualsAndHashCode(callSuper = false)
public class ModelViewFilter extends QueryFilter<ModelViewFilter, ModelView> {}
