package cn.asany.shanhai.core.graphql;

import cn.asany.shanhai.core.convert.ModelConverter;
import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.graphql.enums.EndpointIdType;
import cn.asany.shanhai.core.graphql.enums.ModelIdType;
import cn.asany.shanhai.core.graphql.inputs.*;
import cn.asany.shanhai.core.graphql.types.ModelConnection;
import cn.asany.shanhai.core.service.ModelService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
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
public class ModelGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final ModelService modelService;
  private final ModelConverter modelConverter;

  public ModelGraphQLRootResolver(ModelService modelService, ModelConverter modelConverter) {
    this.modelService = modelService;
    this.modelConverter = modelConverter;
  }

  public Model createModel(ModelCreateInput input) {
    Model model = this.modelConverter.toModel(input);
    return modelService.save(model);
  }

  public Model updateModel(Long id, ModelUpdateInput input) {
    Model model = this.modelConverter.toModel(input);
    return modelService.update(id, model);
  }

  public Boolean deleteModel(Long id) {
    modelService.delete(id);
    return Boolean.TRUE;
  }

  public List<Model> models(
      Long module, ModelWhereInput where, int offset, int limit, Sort orderBy) {
    return modelService.findAll(
        where.toFilter().equal("module.id", module), offset, limit, orderBy);
  }

  public ModelConnection modelsConnection(
      Long module, ModelWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        modelService.findPage(pageable, where.toFilter().equal("module.id", module)),
        ModelConnection.class);
  }

  public Optional<Model> model(String id, ModelIdType idType) {
    if (idType == ModelIdType.id) {
      return modelService.findById(Long.valueOf(id));
    }
    return modelService.findByCode(id);
  }

  public Optional<ModelField> endpoint(String id, EndpointIdType idType) {
    if (idType == EndpointIdType.id) {
      return modelService.findEndpointById(Long.valueOf(id));
    }
    return modelService.findEndpointByCode(id);
  }

  public ModelField addModelField(Long modelId, ModelFieldInput input) {
    ModelField field = this.modelConverter.toField(input);
    return modelService.addField(modelId, field);
  }

  public ModelField updateModelField(Long modelId, Long fieldId, ModelFieldInput input) {
    ModelField field = this.modelConverter.toField(input);
    return modelService.updateField(modelId, fieldId, field);
  }

  public Boolean removeModelField(Long modelId, Long fieldId) {
    modelService.removeField(modelId, fieldId);
    return Boolean.TRUE;
  }

  public List<ModelField> modelFields(
      Long model, ModelFieldWhereInput where, int offset, int limit, Sort sort) {
    return modelService.listModelFields(
        where.toFilter().equal("model.id", model), offset, limit, sort);
  }
}
