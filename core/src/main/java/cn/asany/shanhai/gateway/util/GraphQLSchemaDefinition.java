package cn.asany.shanhai.gateway.util;


import cn.asany.shanhai.gateway.util.GraphQLTypeDefinition.GraphQLTypeDefinitionBuilder;
import graphql.language.*;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.SchemaPrinter;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.UnExecutableSchemaGenerator;
import org.jfantasy.framework.util.common.ObjectUtil;

import java.util.*;

import static graphql.schema.idl.SchemaPrinter.Options.defaultOptions;


public class GraphQLSchemaDefinition {

    private transient final Map<String, GraphQLTypeDefinition> typeMap = new LinkedHashMap<>();

    private TypeDefinitionRegistry registry;

    private GraphQLTypeDefinition queryType;
    private GraphQLTypeDefinition mutationType;
    private GraphQLTypeDefinition subscriptionType;

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
        if ("Query".equals(name)) {
            return queryType;
        }
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

    public GraphQLTypeDefinition getQueryType() {
        return this.queryType;
    }

    public GraphQLTypeDefinition getMutationType() {
        return this.mutationType;
    }

    public GraphQLTypeDefinition getSubscriptionType() {
        return this.subscriptionType;
    }

    private GraphQLTypeDefinition buildGraphQLType(InputObjectTypeDefinition definition) {
        return GraphQLTypeDefinition.builder().schema(this).id(definition.getName()).type(GraphQLType.InputObject).build();
    }

    private GraphQLTypeDefinition buildGraphQLType(InterfaceTypeDefinition definition) {
        GraphQLTypeDefinitionBuilder builder = GraphQLTypeDefinition.builder().schema(this).id(definition.getName()).type(GraphQLType.Interface);

        for (FieldDefinition field : definition.getFieldDefinitions()) {
            builder.field(field.getName(), field);
        }

        String description = definition.getDescription() != null ? definition.getDescription().content : null;
        builder.description(description);

        return builder.build();
    }

    private GraphQLTypeDefinition buildGraphQLType(ObjectTypeDefinition definition) {
        GraphQLTypeDefinitionBuilder builder = GraphQLTypeDefinition.builder().schema(this).id(definition.getName()).type(GraphQLType.Object);

        for (FieldDefinition field : definition.getFieldDefinitions()) {
            builder.field(field.getName(), field);
        }

        String description = definition.getDescription() != null ? definition.getDescription().content : null;
        builder.description(description);

        return builder.build();
    }

    private GraphQLTypeDefinition buildGraphQLType(ScalarTypeDefinition definition) {
        return GraphQLTypeDefinition.builder().schema(this).id(definition.getName()).type(GraphQLType.Scalar).build();
    }

    private GraphQLTypeDefinition buildGraphQLType(EnumTypeDefinition definition) {
        return GraphQLTypeDefinition.builder().schema(this).id(definition.getName()).type(GraphQLType.Enum).build();
    }

    private GraphQLTypeDefinition buildGraphQLType(UnionTypeDefinition definition) {
        return GraphQLTypeDefinition.builder().schema(this).id(definition.getName()).type(GraphQLType.Union).build();
    }

    public void removeType(String type) {
        GraphQLTypeDefinition definition;
        if (type == "Query") {
            definition = queryType;
        } else if (type == "Mutation") {
            definition = mutationType;
        } else if (type == "Subscription") {
            definition = subscriptionType;
        } else {
            definition = typeMap.get(type);
        }
        this.removeType(definition);
    }

    public void removeType(GraphQLTypeDefinition type) {
        this.typeMap.remove(type.getId());
        this.registry.remove(this.registry.getType(type.getId()).get());
    }

    public void removeField(GraphQLTypeDefinition type, GraphQLFieldDefinition definition) {
        ObjectTypeDefinition query = (ObjectTypeDefinition) this.registry.getType(type.getId()).get();
        ObjectTypeDefinition newQuery = ObjectTypeDefinition.newObjectTypeDefinition()
            .name(query.getName())
            .implementz(query.getImplements())
            .directives(query.getDirectives())
            .fieldDefinitions(ObjectUtil.filter(query.getFieldDefinitions(), (item) -> !item.getName().equals(definition.getId())))
            .description(query.getDescription())
            .sourceLocation(query.getSourceLocation())
            .comments(query.getComments())
            .ignoredChars(query.getIgnoredChars())
            .additionalData(query.getAdditionalData())
            .build();
        this.registry.remove(query);
        this.registry.add(newQuery);
    }

    public String print() {
        SchemaPrinter.Options noDirectivesOption = defaultOptions().includeDirectives(false);
        SchemaPrinter schemaPrinter = new SchemaPrinter(noDirectivesOption);
        GraphQLSchema graphQLSchema = UnExecutableSchemaGenerator.makeUnExecutableSchema(this.registry);
        return schemaPrinter.print(graphQLSchema);
    }

    public Set<GraphQLTypeDefinition> dependencies(String name) {
        String names[] = name.split("\\.");
        String type = names[0];

        GraphQLTypeDefinition typeDefinition = this.getType(type);

        Set<GraphQLTypeDefinition> origin = new HashSet<>();

        if (names.length > 1) {
            String field = names[1];
            GraphQLFieldDefinition fieldDefinition = typeDefinition.getField(field);
            origin.add(fieldDefinition.getTypeDefinition());
        } else {
            origin.add(typeDefinition);
        }

        Map<String, GraphQLTypeDefinition> book = new HashMap<>();
        recursive(origin, book);
        return new HashSet<>(book.values());
    }

    public void recursive(Set<GraphQLTypeDefinition> definitions, Map<String, GraphQLTypeDefinition> book) {
        for (GraphQLTypeDefinition definition : definitions) {
            if (book.containsKey(definition.getId())) {
                continue;
            }
            book.put(definition.getId(), definition);
            System.out.println("-------" + definition.getId());
            if (definition.getType() == GraphQLType.Scalar) {
                continue;
            }
            Set<GraphQLTypeDefinition> origin = new HashSet<>();
            for (GraphQLFieldDefinition field : definition.getFields()) {
                if (book.containsKey(field.getType())) {
                    continue;
                }
                origin.add(field.getTypeDefinition());
            }
            recursive(origin, book);
        }
    }

    public static class GraphQLSchemaBuilder {
        private String schemaInput;
        private List<String> parsing = new ArrayList<>();

        public GraphQLSchemaBuilder schema(String schemaInput) {
            this.schemaInput = schemaInput;
            return this;
        }

        public GraphQLSchemaDefinition build() {
            GraphQLSchemaDefinition schema = new GraphQLSchemaDefinition();

            SchemaParser schemaParser = new SchemaParser();
            TypeDefinitionRegistry registry = schemaParser.parse(this.schemaInput);

            loadSchema(registry, schema);

            return schema;
        }

        private void loadSchema(TypeDefinitionRegistry registry, GraphQLSchemaDefinition schema) {
            GraphQLTypeDefinitionBuilder queryTypeBuilder = GraphQLTypeDefinition.builder().schema(schema).id("Query").type(GraphQLType.Object);
            GraphQLTypeDefinitionBuilder mutationTypeBuilder = GraphQLTypeDefinition.builder().schema(schema).id("Mutation").type(GraphQLType.Object);
            GraphQLTypeDefinitionBuilder subscriptionTypeBuilder = GraphQLTypeDefinition.builder().schema(schema).id("Subscription").type(GraphQLType.Object);

            List<FieldDefinition> queries = registry.getType("Query", ObjectTypeDefinition.class).get().getFieldDefinitions();
            List<FieldDefinition> mutations = registry.getType("Mutation", ObjectTypeDefinition.class).get().getFieldDefinitions();
            List<FieldDefinition> subscriptions = registry.getType("Subscription", ObjectTypeDefinition.class).get().getFieldDefinitions();

            schema.setTypeDefinitionRegistry(registry);

            for (FieldDefinition field : queries) {
                String name = typeName(field.getType());
                loadTypeDefinition(registry.getType(name).get(), registry, schema);
                queryTypeBuilder.field(field.getName(), field);
            }

            for (FieldDefinition field : mutations) {
                String name = typeName(field.getType());
                loadTypeDefinition(registry.getType(name).get(), registry, schema);
                mutationTypeBuilder.field(field.getName(), field);
            }

            for (FieldDefinition field : subscriptions) {
                String name = typeName(field.getType());
                loadTypeDefinition(registry.getType(name).get(), registry, schema);
                subscriptionTypeBuilder.field(field.getName(), field);
            }

            schema.setQueryType(queryTypeBuilder.build());
            schema.setMutationType(mutationTypeBuilder.build());
            schema.setSubscriptionType(subscriptionTypeBuilder.build());
        }

        private void loadTypeDefinition(TypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchemaDefinition schema) {
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

        private void loadInterfaceTypeDefinition(InterfaceTypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchemaDefinition schema) {
            if (this.parsing.contains(definition.getName())) {
                return;
            }
            this.parsing.add(definition.getName());

            loadFieldDefinitions(definition.getFieldDefinitions(), registry, schema);

            schema.addType(definition);
            this.parsing.remove(definition.getName());
        }

        private void loadUnionTypeDefinition(UnionTypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchemaDefinition schema) {
            if (this.parsing.contains(definition.getName())) {
                return;
            }
            for (Type type : definition.getMemberTypes()) {
                loadTypeDefinition(registry.getType(type).get(), registry, schema);
            }
            schema.addType(definition);
            this.parsing.remove(definition.getName());
        }

        private void loadEnumTypeDefinition(EnumTypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchemaDefinition schema) {
            schema.addType(definition);
        }

        private void loadScalarTypeDefinition(ScalarTypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchemaDefinition schema) {
            schema.addType(definition);
        }


        private void loadObjectTypeDefinition(ObjectTypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchemaDefinition schema) {
            if (this.parsing.contains(definition.getName())) {
                System.out.println("循环依赖:" + definition);
                return;
            }
            this.parsing.add(definition.getName());

            loadFieldDefinitions(definition.getFieldDefinitions(), registry, schema);

            schema.addType(definition);
            GraphQLTypeDefinition type = schema.getType(definition.getName());
//          TODO:  System.out.println("新增 ModelType : " + definition.getName() + " --- " + type.getBoost());
            this.parsing.remove(definition.getName());
        }

        private void loadFieldDefinitions(List<FieldDefinition> fields, TypeDefinitionRegistry registry, GraphQLSchemaDefinition schema) {
            for (FieldDefinition field : fields) {
                String name = typeName(field.getType());
                loadTypeDefinition(registry.getType(name).get(), registry, schema);
            }
        }

    }

    private void setTypeDefinitionRegistry(TypeDefinitionRegistry registry) {
        this.registry = registry;
    }

    private void setSubscriptionType(GraphQLTypeDefinition type) {
        this.subscriptionType = type;
    }

    private void setMutationType(GraphQLTypeDefinition type) {
        this.mutationType = type;
    }

    private void setQueryType(GraphQLTypeDefinition type) {
        this.queryType = type;
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
