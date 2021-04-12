package cn.asany.shanhai.schema.util;

import graphql.language.FieldDefinition;
import lombok.Builder;
import lombok.Data;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author limaofeng
 */
@Data
@Builder
public class GraphQLTypeDefinition {
    private String id;
    private String description;
    private GraphQLType type;
    private int boost;
    private Map<String, GraphQLFieldDefinition> fieldMap;

    @Transient
    public List<GraphQLFieldDefinition> getFields() {
        return new ArrayList<>(this.fieldMap.values());
    }

    public void remove(GraphQLFieldDefinition definition) {
        this.fieldMap.remove(definition.getId());
    }

    public static class GraphQLTypeDefinitionBuilder {

        public GraphQLTypeDefinitionBuilder field(String name, FieldDefinition field) {
            if (this.fieldMap == null) {
                this.fieldMap = new LinkedHashMap<>();
            }
            String description = field.getDescription() != null ? field.getDescription().content : null;
            this.fieldMap.put(name, GraphQLFieldDefinition.builder().id(name).type(field.getType()).description(description).build());
            return this;
        }

    }
}
