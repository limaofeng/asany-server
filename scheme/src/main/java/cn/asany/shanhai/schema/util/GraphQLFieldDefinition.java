package cn.asany.shanhai.schema.util;

import graphql.language.Type;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraphQLFieldDefinition {
    private String id;
    private String description;
    private String type;

    public static class GraphQLFieldDefinitionBuilder {

        public GraphQLFieldDefinitionBuilder type(Type type) {
            this.type = GraphQLSchemaDefinition.typeName(type);
            return this;
        }

    }

}
