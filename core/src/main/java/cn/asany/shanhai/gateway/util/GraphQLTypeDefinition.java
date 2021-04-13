package cn.asany.shanhai.gateway.util;

import graphql.language.FieldDefinition;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(of = {"id", "description", "type", "fieldMap"})
public class GraphQLTypeDefinition {
    private String id;
    private String description;
    private GraphQLType type;
    private transient GraphQLSchemaDefinition schema;
    private int boost;
    private Map<String, GraphQLFieldDefinition> fieldMap;

    @Transient
    public GraphQLSchemaDefinition getSchema() {
        return schema;
    }

    @Transient
    public List<GraphQLFieldDefinition> getFields() {
        if (this.fieldMap == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.fieldMap.values());
    }

    public void remove(GraphQLFieldDefinition definition) {
        this.fieldMap.remove(definition.getId());
    }

    public GraphQLFieldDefinition getField(String field) {
        return fieldMap.get(field);
    }

    public static class GraphQLTypeDefinitionBuilder {

        public GraphQLTypeDefinitionBuilder field(String name, FieldDefinition field) {
            if (this.fieldMap == null) {
                this.fieldMap = new LinkedHashMap<>();
            }
            String description = field.getDescription() != null ? field.getDescription().content : null;
            this.fieldMap.put(name, GraphQLFieldDefinition.builder().schema(this.schema).id(name).type(field.getType()).description(description).build());
            return this;
        }

    }
}
