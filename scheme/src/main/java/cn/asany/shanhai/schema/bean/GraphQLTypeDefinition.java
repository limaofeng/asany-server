package cn.asany.shanhai.schema.bean;

import graphql.language.FieldDefinition;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class GraphQLTypeDefinition {
    private String id;
    private String description;
    private GraphQLType type;
    private int boost;
    private List<GraphQLFieldDefinition> fields;

    public static class GraphQLTypeDefinitionBuilder {

        public GraphQLTypeDefinitionBuilder field(String name, FieldDefinition field) {
            if (this.fields == null) {
                this.fields = new ArrayList<>();
            }
            String description = field.getDescription() != null ? field.getDescription().content : null;
            this.fields.add(GraphQLFieldDefinition.builder().id(name).type(field.getType()).description(description).build());
            return this;
        }

    }
}
