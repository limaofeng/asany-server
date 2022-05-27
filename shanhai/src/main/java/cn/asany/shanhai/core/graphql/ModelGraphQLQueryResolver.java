package cn.asany.shanhai.core.graphql;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.graphql.enums.EndpointIdType;
import cn.asany.shanhai.core.graphql.enums.ModelIdType;
import cn.asany.shanhai.core.graphql.inputs.ModelFilter;
import cn.asany.shanhai.core.graphql.types.ModelConnection;
import cn.asany.shanhai.core.service.ModelService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 模型
 *
 * @author limaofeng
 */
@Component
public class ModelGraphQLQueryResolver implements GraphQLQueryResolver {

  private final ModelService modelService;

  public ModelGraphQLQueryResolver(ModelService modelService) {
    this.modelService = modelService;
  }

  public List<Model> models(ModelFilter filter, int first, int offset, Sort orderBy) {
    Pageable pageable = PageRequest.of(offset, first, orderBy);
    filter = ObjectUtil.defaultValue(filter, new ModelFilter());
    return modelService.findPage(pageable, filter.build()).getContent();
  }

  public ModelConnection modelsConnection(
      ModelFilter filter, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    filter = ObjectUtil.defaultValue(filter, new ModelFilter());
    return Kit.connection(modelService.findPage(pageable, filter.build()), ModelConnection.class);
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
