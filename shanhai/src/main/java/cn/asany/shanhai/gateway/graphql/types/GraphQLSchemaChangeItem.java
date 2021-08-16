package cn.asany.shanhai.gateway.graphql.types;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraphQLSchemaChangeItem {

  private String id;

  private String description;

  private String type;

  private String status;

  @Override
  public String toString() {
    return "GraphQLSchemaChangeItem{"
        + "id='"
        + id
        + '\''
        + ", description='"
        + description
        + '\''
        + ", type='"
        + type
        + '\''
        + ", status='"
        + status
        + '\''
        + '}';
  }
}
