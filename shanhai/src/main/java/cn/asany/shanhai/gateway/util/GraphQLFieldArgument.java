package cn.asany.shanhai.gateway.util;

import graphql.language.*;
import java.beans.Transient;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(of = {"id", "description", "type"})
public class GraphQLFieldArgument {
  private String id;
  private String description;
  private String type;
  private Object defaultValue;
  private GraphQLSchema schema;

  @Transient
  public GraphQLSchema getSchema() {
    return schema;
  }

  @Transient
  public GraphQLObjectType getTypeDefinition() {
    return this.schema.getType(this.type);
  }

  public static class GraphQLFieldArgumentBuilder {

    public GraphQLFieldArgumentBuilder defaultValue(Value value) {
      if (value == null) {
        return this;
      }
      if (value instanceof IntValue) {
        this.defaultValue = ((IntValue) value).getValue().longValue();
      } else if (value instanceof BooleanValue) {
        this.defaultValue = ((BooleanValue) value).isValue();
      } else if (value instanceof EnumValue) {
        this.defaultValue = ((EnumValue) value).getName();
      } else if (value instanceof FloatValue) {
        this.defaultValue = ((FloatValue) value).getValue();
      } else if (value instanceof StringValue) {
        this.defaultValue = ((StringValue) value).getValue();
      } else if (value instanceof ObjectValue) {
        this.defaultValue = ((ObjectValue) value).getObjectFields();
      } else {
        throw new RuntimeException("暂不支持" + value.getClass());
      }
      return this;
    }
  }
}
