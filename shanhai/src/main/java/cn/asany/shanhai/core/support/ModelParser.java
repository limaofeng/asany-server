package cn.asany.shanhai.core.support;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.domain.enums.ModelType;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import cn.asany.shanhai.core.support.graphql.ModelDataFetcher;
import cn.asany.shanhai.core.support.graphql.ModelDelegateFactory;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import graphql.schema.DataFetcher;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

public class ModelParser {

  @Autowired FieldTypeRegistry fieldTypeRegistry;
  @Autowired private ModelService modelService;
  @Autowired private ModelSessionFactory modelSessionFactory;
  @Autowired private ModelDelegateFactory delegateFactory;
  private final Map<Long, ModelResource> modelResourceMap = new ConcurrentHashMap<>();
  private final Map<String, DataFetcher<Object>> dataFetcherMap = new ConcurrentHashMap<>();

  public List<Model> getModels() {
    return modelResourceMap.values().stream()
        .map(ModelResource::getModel)
        .collect(Collectors.toList());
  }

  public void init() {
    List<Model> models = this.modelService.findEntityModels();
    for (Model model : models) {
      model = this.modelService.getDetails(model.getId());

      ModelRepository modelRepository = modelSessionFactory.buildModelRepository(model);

      this.modelResourceMap.put(
          model.getId(), ModelResource.builder().model(model).repository(modelRepository).build());

      for (ModelEndpoint endpoint : model.getEndpoints()) {
        dataFetcherMap.put(
            model.getCode() + "." + endpoint.getCode(),
            new ModelDataFetcher(
                delegateFactory.build(model, endpoint, modelRepository, endpoint.getDelegate())));
      }
    }
  }

  public DataFetcher<Object> getDataFetcher(String key) {
    return this.dataFetcherMap.get(key);
  }

  public FieldTypeRegistry getFieldTypeRegistry() {
    return fieldTypeRegistry;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ModelResource {
    private Model model;
    private ModelRepository repository;
  }
}
