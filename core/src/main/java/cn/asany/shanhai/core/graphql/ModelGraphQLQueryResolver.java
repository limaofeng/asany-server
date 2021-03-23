package cn.asany.shanhai.core.graphql;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.graphql.inputs.ModelFilter;
import cn.asany.shanhai.core.graphql.types.ModelConnection;
import cn.asany.shanhai.core.service.ModelService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author limaofeng
 */
@Component
public class ModelGraphQLQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ModelService modelService;

    public ModelConnection models(ModelFilter filter, int page, int pageSize, OrderBy orderBy) {
        Pager<Model> pager = new Pager<>(page, pageSize, orderBy);
        filter = ObjectUtil.defaultValue(filter, new ModelFilter());
        return Kit.connection(modelService.findPager(pager, filter.build()), ModelConnection.class);
    }

    public Optional<Model> model(Long id) {
        return modelService.get(id);
    }

}
