package cn.asany.shanhai.gateway.util;

import graphql.language.FieldDefinition;
import graphql.language.InputValueDefinition;
import java.beans.Transient;
import java.util.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** @author limaofeng */
@Data
@Builder
@EqualsAndHashCode(of = {"id", "description", "type", "fieldMap"})
public class GraphQLObjectType {
  private String id;
  private String description;
  private GraphQLType type;
  private List<String> implementz;
  private List<String> memberTypes;
  private transient GraphQLSchema schema;
  private int boost;
  private Map<String, GraphQLField> fieldMap;

  @Transient
  public GraphQLSchema getSchema() {
    return schema;
  }

  @Transient
  public List<GraphQLField> getFields() {
    if (this.fieldMap == null) {
      return new ArrayList<>();
    }
    return new ArrayList<>(this.fieldMap.values());
  }

  public void remove(GraphQLField definition) {
    this.fieldMap.remove(definition.getId());
  }

  public GraphQLField getField(String field) {
    return fieldMap.get(field);
  }

  public static class GraphQLObjectTypeBuilder {

    public GraphQLObjectTypeBuilder() {
      this.implementz = Collections.emptyList();
      this.memberTypes = Collections.emptyList();
    }

    public GraphQLObjectTypeBuilder implementz(List<String> types) {
      this.implementz = types;
      return this;
    }

    public GraphQLObjectTypeBuilder memberTypes(List<String> types) {
      this.memberTypes = types;
      return this;
    }

    public GraphQLObjectTypeBuilder field(String name, FieldDefinition field) {
      if (this.fieldMap == null) {
        this.fieldMap = new LinkedHashMap<>();
      }
      String description = field.getDescription() != null ? field.getDescription().content : null;

      this.fieldMap.put(
          name,
          GraphQLField.builder()
              .schema(this.schema)
              .id(name)
              .arguments(field.getInputValueDefinitions())
              .type(field.getType())
              .required(GraphQLSchema.isRequired(field.getType()))
              .list(GraphQLSchema.isList(field.getType()))
              .description(description)
              .build());
      return this;
    }

    public GraphQLObjectTypeBuilder field(String name, InputValueDefinition field) {
      if (this.fieldMap == null) {
        this.fieldMap = new LinkedHashMap<>();
      }
      String description = field.getDescription() != null ? field.getDescription().content : null;
      this.fieldMap.put(
          name,
          GraphQLField.builder()
              .schema(this.schema)
              .id(name)
              .type(field.getType())
              .description(description)
              .build());
      return this;
    }
  }
}
