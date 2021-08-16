package cn.asany.shanhai.core.graphql;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.graphql.enums.EndpointIdType;
import cn.asany.shanhai.core.graphql.enums.ModelIdType;
import cn.asany.shanhai.core.graphql.inputs.ModelFilter;
import cn.asany.shanhai.core.graphql.types.ModelConnection;
import cn.asany.shanhai.core.service.ModelService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Optional;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author limaofeng */
@Component
public class ModelGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private ModelService modelService;

  public ModelConnection models(ModelFilter filter, int page, int pageSize, OrderBy orderBy) {
    Pager<Model> pager = new Pager<>(page, pageSize, orderBy);
    filter = ObjectUtil.defaultValue(filter, new ModelFilter());
    return Kit.connection(modelService.findPager(pager, filter.build()), ModelConnection.class);
  }

  public Optional<Model> model(String id, ModelIdType idType) {
    if (idType == ModelIdType.id) {
      return modelService.get(Long.valueOf(id));
    }
    return modelService.findByCode(id);
  }

  public Optional<ModelField> endpoint(String id, EndpointIdType idType) {
    if (idType == EndpointIdType.id) {
      return modelService.findEndpointById(Long.valueOf(id));
    }
    return modelService.findEndpointByCode(id);
  }
}
