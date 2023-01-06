package cn.asany.shanhai.core.graphql.resolver;

import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class ModelFieldGraphQLResolver implements GraphQLResolver<ModelField> {

  private final FieldTypeRegistry fieldTypeRegistry;

  public ModelFieldGraphQLResolver(FieldTypeRegistry fieldTypeRegistry) {
    this.fieldTypeRegistry = fieldTypeRegistry;
  }

  public Integer index(ModelField modelField) {
    return modelField.getSort();
  }

  public FieldType<?, ?> type(ModelField modelField) {
    return fieldTypeRegistry.getType(modelField.getType());
  }
}
