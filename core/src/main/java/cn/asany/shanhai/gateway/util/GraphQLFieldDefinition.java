package cn.asany.shanhai.gateway.util;

import graphql.language.Type;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.Transient;

@Data
@Builder
@EqualsAndHashCode(of = {"id", "description", "type"})
public class GraphQLFieldDefinition {
    private String id;
    private String description;
    private String type;
    private GraphQLSchemaDefinition schema;

    @Transient
    public GraphQLSchemaDefinition getSchema() {
        return schema;
    }

    @Transient
    public GraphQLTypeDefinition getTypeDefinition() {
        return this.schema.getType(this.type);
    }

    public static class GraphQLFieldDefinitionBuilder {

        public GraphQLFieldDefinitionBuilder type(Type type) {
            this.type = GraphQLSchemaDefinition.typeName(type);
            return this;
        }

    }

}
