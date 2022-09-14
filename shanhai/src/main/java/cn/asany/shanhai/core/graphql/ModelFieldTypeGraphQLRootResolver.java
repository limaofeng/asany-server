package cn.asany.shanhai.core.graphql;

import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * 模型
 *
 * @author limaofeng
 */
@Component
public class ModelFieldTypeGraphQLRootResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final FieldTypeRegistry fieldTypeRegistry;

  public ModelFieldTypeGraphQLRootResolver(FieldTypeRegistry fieldTypeRegistry) {
    this.fieldTypeRegistry = fieldTypeRegistry;
  }

  public List<FieldType<?, ?>> modelFiledTypes() {
    return fieldTypeRegistry.types().stream()
        .filter(item -> item.getFamily() != null)
        .sorted(Comparator.comparingInt(FieldType::getIndex))
        .collect(Collectors.toList());
  }
}
