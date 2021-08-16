package cn.asany.shanhai.gateway.util;

import graphql.language.InputValueDefinition;
import graphql.language.Type;
import java.beans.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(of = {"id", "description", "type", "arguments"})
public class GraphQLField {
  private String id;
  private String description;
  private String type;
  private boolean list;
  private boolean required;
  private GraphQLSchema schema;
  private Map<String, GraphQLFieldArgument> arguments;

  @Transient
  public GraphQLSchema getSchema() {
    return schema;
  }

  @Transient
  public GraphQLObjectType getTypeDefinition() {
    return this.schema.getType(this.type);
  }

  public static class GraphQLFieldBuilder {

    public GraphQLFieldBuilder() {
      this.arguments = new HashMap<>();
    }

    public GraphQLFieldBuilder type(Type type) {
      this.type = GraphQLSchema.typeName(type);
      return this;
    }

    public GraphQLFieldBuilder arguments(List<InputValueDefinition> inputValueDefinitions) {
      Map<String, GraphQLFieldArgument> arguments = new HashMap<>();
      for (InputValueDefinition input : inputValueDefinitions) {
        String description = input.getDescription() != null ? input.getDescription().content : null;
        arguments.put(
            input.getName(),
            GraphQLFieldArgument.builder()
                .schema(this.schema)
                .id(input.getName())
                .type(GraphQLSchema.typeName(input.getType()))
                .description(description)
                .defaultValue(input.getDefaultValue())
                .build());
      }
      this.arguments = arguments;
      return this;
    }
  }
}
