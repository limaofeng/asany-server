package cn.asany.shanhai.schema.bean;

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
            this.type = GraphQLSchema.typeName(type);
            return this;
        }

    }

}
