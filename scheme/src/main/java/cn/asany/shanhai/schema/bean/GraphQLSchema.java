package cn.asany.shanhai.schema.bean;


import cn.asany.shanhai.schema.bean.GraphQLTypeDefinition.GraphQLTypeDefinitionBuilder;
import graphql.language.*;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.util.*;

public class GraphQLSchema {

    private final Map<String, GraphQLTypeDefinition> typeMap = new LinkedHashMap<>();

    public static GraphQLSchemaBuilder builder() {
        return new GraphQLSchemaBuilder();
    }

    public Map<String, GraphQLTypeDefinition> getTypeMap() {
        return typeMap;
    }

    private void addType(InterfaceTypeDefinition definition) {
        this.typeMap.put(definition.getName(), buildGraphQLType(definition));
    }

    private void addType(UnionTypeDefinition definition) {
        this.typeMap.put(definition.getName(), buildGraphQLType(definition));
    }

    public void addType(InputObjectTypeDefinition definition) {
        this.typeMap.put(definition.getName(), buildGraphQLType(definition));
    }

    private void addType(EnumTypeDefinition definition) {
        this.typeMap.put(definition.getName(), buildGraphQLType(definition));
    }

    public void addType(ObjectTypeDefinition definition) {
        this.typeMap.put(definition.getName(), buildGraphQLType(definition));
    }

    public void addType(ScalarTypeDefinition definition) {
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

    public static class GraphQLSchemaBuilder {
        private String schemaInput;
        private List<String> parsing = new ArrayList<>();

        public GraphQLSchemaBuilder schema(String schemaInput) {
            this.schemaInput = schemaInput;
            return this;
        }

        public GraphQLSchema build() {
            GraphQLSchema schema = new GraphQLSchema();

            SchemaParser schemaParser = new SchemaParser();
            TypeDefinitionRegistry registry = schemaParser.parse(this.schemaInput);

            loadSchema(registry, schema);

            return schema;
        }

        private void loadSchema(TypeDefinitionRegistry registry, GraphQLSchema schema) {
            List<FieldDefinition> queries = registry.getType("Query", ObjectTypeDefinition.class).get().getFieldDefinitions();

            for (FieldDefinition field : queries) {
                String name = typeName(field.getType());
                loadTypeDefinition(registry.getType(name).get(), registry, schema);
            }

        }

        private void loadTypeDefinition(TypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchema schema) {
            if (schema.getType(definition.getName()) != null) {
                return;
            }
            if (definition instanceof ObjectTypeDefinition) {
                loadObjectTypeDefinition((ObjectTypeDefinition) definition, registry, schema);
            } else if (definition instanceof ScalarTypeDefinition) {
                loadScalarTypeDefinition((ScalarTypeDefinition) definition, registry, schema);
            } else if (definition instanceof EnumTypeDefinition) {
                loadEnumTypeDefinition((EnumTypeDefinition) definition, registry, schema);
            } else if (definition instanceof UnionTypeDefinition) {
                loadUnionTypeDefinition((UnionTypeDefinition) definition, registry, schema);
            } else if (definition instanceof InterfaceTypeDefinition) {
                loadInterfaceTypeDefinition((InterfaceTypeDefinition) definition, registry, schema);
            } else {
                System.out.println(">-->-<--<" + definition.getName());
            }

        }

        private void loadInterfaceTypeDefinition(InterfaceTypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchema schema) {
            if (this.parsing.contains(definition.getName())) {
                return;
            }
            this.parsing.add(definition.getName());

            loadFieldDefinitions(definition.getFieldDefinitions(), registry, schema);

            schema.addType(definition);
            this.parsing.remove(definition.getName());
        }

        private void loadUnionTypeDefinition(UnionTypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchema schema) {
            if (this.parsing.contains(definition.getName())) {
                return;
            }
            for (Type type : definition.getMemberTypes()) {
                loadTypeDefinition(registry.getType(type).get(), registry, schema);
            }
            schema.addType(definition);
            this.parsing.remove(definition.getName());
        }

        private void loadEnumTypeDefinition(EnumTypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchema schema) {
            schema.addType(definition);
        }

        private void loadScalarTypeDefinition(ScalarTypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchema schema) {
            schema.addType(definition);
        }


        private void loadObjectTypeDefinition(ObjectTypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchema schema) {
            if (this.parsing.contains(definition.getName())) {
                System.out.println("循环依赖:" + definition);
                return;
            }
            this.parsing.add(definition.getName());

            loadFieldDefinitions(definition.getFieldDefinitions(), registry, schema);

            schema.addType(definition);
            GraphQLTypeDefinition type = schema.getType(definition.getName());
            System.out.println("新增 ModelType : " + definition.getName() + " --- " + type.getBoost());
            this.parsing.remove(definition.getName());
        }

        private void loadFieldDefinitions(List<FieldDefinition> fields, TypeDefinitionRegistry registry, GraphQLSchema schema) {
            for (FieldDefinition field : fields) {
                String name = typeName(field.getType());
                loadTypeDefinition(registry.getType(name).get(), registry, schema);
            }
        }

    }

    public static String typeName(Type type) {
        if (type instanceof NonNullType) {
            type = ((NonNullType) type).getType();
        }
        if (type instanceof ListType) {
            type = ((ListType) type).getType();
        }
        return ((TypeName) type).getName();
    }

}
