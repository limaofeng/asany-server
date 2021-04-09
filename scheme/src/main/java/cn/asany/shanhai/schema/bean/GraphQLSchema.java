package cn.asany.shanhai.schema.bean;


import cn.asany.shanhai.schema.bean.GraphQLTypeDefinition.GraphQLTypeDefinitionBuilder;
import graphql.language.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphQLSchema {

    private final Map<String, GraphQLTypeDefinition> typeMap = new HashMap<>();

    public Map<String, GraphQLTypeDefinition> getTypeMap() {
        return typeMap;
    }

    public void addType(InputObjectTypeDefinition definition) {
        this.typeMap.put(definition.getName(), buildGraphQLType(definition));
    }

    public void addType(ObjectTypeDefinition definition) {
        this.typeMap.put(definition.getName(), buildGraphQLType(definition));
    }

    public GraphQLTypeDefinition getType(String name) {
        return this.typeMap.get(name);
    }

    public void addScalars(Map<String, ScalarTypeDefinition> scalars) {
        for (Map.Entry<String, ScalarTypeDefinition> entry : scalars.entrySet()) {
            this.typeMap.put(entry.getKey(), buildGraphQLType(entry.getValue()));
        }
    }

    public void addEnums(List<EnumTypeDefinition> enums) {
        for (EnumTypeDefinition definition : enums) {
            this.typeMap.put(definition.getName(), buildGraphQLType(definition));
        }
    }

    public void addUnions(List<UnionTypeDefinition> enums) {
        for (UnionTypeDefinition definition : enums) {
            this.typeMap.put(definition.getName(), buildGraphQLType(definition));
        }
    }

    public void addInterfaces(List<InterfaceTypeDefinition> interfaces) {
        for (InterfaceTypeDefinition definition : interfaces) {
            this.typeMap.put(definition.getName(), buildGraphQLType(definition));
        }
    }

    private GraphQLTypeDefinition buildGraphQLType(InputObjectTypeDefinition definition) {
        return GraphQLTypeDefinition.builder().id(definition.getName()).type(GraphQLType.InputObject).build();
    }

    private GraphQLTypeDefinition buildGraphQLType(InterfaceTypeDefinition definition) {
        return GraphQLTypeDefinition.builder().id(definition.getName()).type(GraphQLType.Interface).build();
    }

    private GraphQLTypeDefinition buildGraphQLType(ObjectTypeDefinition definition) {
        GraphQLTypeDefinitionBuilder builder = GraphQLTypeDefinition.builder().id(definition.getName()).type(GraphQLType.Object);

        for (FieldDefinition field : definition.getFieldDefinitions()) {
            builder.field(field.getName(), field);
        }

        String description = definition.getDescription() != null ? definition.getDescription().content : null;
        builder.description(description);

        return builder.build();
    }

    private GraphQLTypeDefinition buildGraphQLType(ScalarTypeDefinition definition) {
        return GraphQLTypeDefinition.builder().id(definition.getName()).type(GraphQLType.Scalar).build();
    }

    private GraphQLTypeDefinition buildGraphQLType(EnumTypeDefinition definition) {
        return GraphQLTypeDefinition.builder().id(definition.getName()).type(GraphQLType.Enum).build();
    }

    private GraphQLTypeDefinition buildGraphQLType(UnionTypeDefinition definition) {
        return GraphQLTypeDefinition.builder().id(definition.getName()).type(GraphQLType.Union).build();
    }

}
