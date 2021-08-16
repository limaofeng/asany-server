package cn.asany.shanhai.core.graphql.inputs;

import cn.asany.shanhai.core.bean.Model;
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
public class ModelFilter extends QueryFilter<ModelFilter, Model> {}
