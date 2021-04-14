package cn.asany.shanhai.gateway.util;

import graphql.language.FieldDefinition;
import graphql.language.InputValueDefinition;
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
public class GraphQLObjectType {
    private String id;
    private String description;
    private GraphQLType type;
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

        public GraphQLObjectTypeBuilder field(String name, FieldDefinition field) {
            if (this.fieldMap == null) {
                this.fieldMap = new LinkedHashMap<>();
            }
            String description = field.getDescription() != null ? field.getDescription().content : null;

            this.fieldMap.put(name, GraphQLField.builder().schema(this.schema).id(name).arguments(field.getInputValueDefinitions()).type(field.getType()).description(description).build());
            return this;
        }

        public GraphQLObjectTypeBuilder field(String name, InputValueDefinition field) {
            if (this.fieldMap == null) {
                this.fieldMap = new LinkedHashMap<>();
            }
            String description = field.getDescription() != null ? field.getDescription().content : null;
            this.fieldMap.put(name, GraphQLField.builder().schema(this.schema).id(name).type(field.getType()).description(description).build());
            return this;
        }

    }
}
