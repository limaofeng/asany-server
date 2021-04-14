package cn.asany.shanhai.gateway.util;


import cn.asany.shanhai.gateway.util.GraphQLObjectType.GraphQLObjectTypeBuilder;
import graphql.language.*;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.SchemaPrinter;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.UnExecutableSchemaGenerator;
import org.jfantasy.framework.util.common.ObjectUtil;

import java.beans.Transient;
import java.util.*;

import static graphql.schema.idl.SchemaPrinter.Options.defaultOptions;


public class GraphQLSchema {

    private transient final Map<String, GraphQLObjectType> typeMap = new LinkedHashMap<>();

    private TypeDefinitionRegistry registry;
    private String content;
    private GraphQLObjectType queryType;
    private GraphQLObjectType mutationType;
    private GraphQLObjectType subscriptionType;

    public static GraphQLSchemaBuilder builder() {
        return new GraphQLSchemaBuilder();
    }

    @Transient
    public String getSource() {
        return content;
    }

    public Map<String, GraphQLObjectType> getTypeMap() {
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

    public GraphQLObjectType getType(String name) {
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

    public GraphQLObjectType getQueryType() {
        return this.queryType;
    }

    public GraphQLObjectType getMutationType() {
        return this.mutationType;
    }

    public GraphQLObjectType getSubscriptionType() {
        return this.subscriptionType;
    }

    private GraphQLObjectTypeBuilder buildGraphQLFields(GraphQLObjectTypeBuilder builder, FieldDefinition[] fieldDefinitions) {
        for (FieldDefinition field : fieldDefinitions) {
            builder.field(field.getName(), field);
        }
        return builder;
    }

    private GraphQLObjectTypeBuilder buildGraphQLFields(GraphQLObjectTypeBuilder builder, InputValueDefinition[] fieldDefinitions) {
        for (InputValueDefinition field : fieldDefinitions) {
            builder.field(field.getName(), field);
        }
        return builder;
    }

    private GraphQLObjectType buildGraphQLType(InterfaceTypeDefinition definition) {
        GraphQLObjectTypeBuilder builder = GraphQLObjectType.builder().schema(this).id(definition.getName()).type(GraphQLType.Interface);

        buildGraphQLFields(builder, definition.getFieldDefinitions().toArray(new FieldDefinition[0]));

        String description = definition.getDescription() != null ? definition.getDescription().content : null;
        builder.description(description);

        return builder.build();
    }

    private GraphQLObjectType buildGraphQLType(ObjectTypeDefinition definition) {
        GraphQLObjectTypeBuilder builder = GraphQLObjectType.builder().schema(this).id(definition.getName()).type(GraphQLType.Object);

        buildGraphQLFields(builder, definition.getFieldDefinitions().toArray(new FieldDefinition[0]));

        String description = definition.getDescription() != null ? definition.getDescription().content : null;
        builder.description(description);

        return builder.build();
    }

    private GraphQLObjectType buildGraphQLType(InputObjectTypeDefinition definition) {
        GraphQLObjectTypeBuilder builder = GraphQLObjectType.builder().schema(this).id(definition.getName()).type(GraphQLType.InputObject);

        buildGraphQLFields(builder, definition.getInputValueDefinitions().toArray(new InputValueDefinition[0]));

        String description = definition.getDescription() != null ? definition.getDescription().content : null;
        builder.description(description);

        return builder.build();
    }

    private GraphQLObjectType buildGraphQLType(ScalarTypeDefinition definition) {
        return GraphQLObjectType.builder().schema(this).id(definition.getName()).type(GraphQLType.Scalar).build();
    }

    private GraphQLObjectType buildGraphQLType(EnumTypeDefinition definition) {
        return GraphQLObjectType.builder().schema(this).id(definition.getName()).type(GraphQLType.Enum).build();
    }

    private GraphQLObjectType buildGraphQLType(UnionTypeDefinition definition) {
        return GraphQLObjectType.builder().schema(this).id(definition.getName()).type(GraphQLType.Union).build();
    }

    public void removeType(String type) {
        GraphQLObjectType definition;
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

    public void removeType(GraphQLObjectType type) {
        this.typeMap.remove(type.getId());
        this.registry.remove(this.registry.getType(type.getId()).get());
    }

    public void removeField(GraphQLObjectType type, GraphQLField definition) {
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
        graphql.schema.GraphQLSchema graphQLSchema = UnExecutableSchemaGenerator.makeUnExecutableSchema(this.registry);
        return schemaPrinter.print(graphQLSchema);
    }

    public Set<String> dependencies(String name) {
        return dependencies(name, new String[0]);
    }

    public Set<String> dependencies(String name, String[] ignoreProperties) {
        String names[] = name.split("\\.");
        String type = names[0];
        Set<String> book = new LinkedHashSet<>();

        GraphQLObjectType typeDefinition = this.getType(type);

        Set<GraphQLObjectType> origin = new HashSet<>();

        if (names.length > 1) {
            String field = names[1];
            GraphQLField fieldDefinition = typeDefinition.getField(field);
            origin.add(fieldDefinition.getTypeDefinition());
            book.add(typeDefinition.getId() + "." + fieldDefinition.getId());
        } else {
            origin.add(typeDefinition);
        }

        recursive(origin, book, new LinkedHashSet<String>(new ArrayList(Arrays.asList(ignoreProperties))));

        List<String> scalars = new ArrayList<>();
        scalars.add("String");
        scalars.add("ID");
        scalars.add("Boolean");
        scalars.add("Float");

        if (names.length == 1) {
            book.add(typeDefinition.getId());
        }

        book.removeAll(scalars);
        return book;
    }

    public void recursive(Set<GraphQLObjectType> definitions, Set<String> book, Set<String> ignoreProperties) {
        for (GraphQLObjectType definition : definitions) {
            if (book.contains(definition.getId())) {
                continue;
            }
            if (ignoreProperties.contains(definition.getId())) {
                continue;
            }
            book.add(definition.getId());
            if (definition.getType() == GraphQLType.Scalar) {
                continue;
            }
            Set<GraphQLObjectType> origin = new HashSet<>();
            for (GraphQLField field : definition.getFields()) {
                String key = definition.getId() + "." + field.getId();
                if (ignoreProperties.contains(key) || ignoreProperties.contains(field.getType())) {
                    continue;
                }
                book.add(key);
                if (book.contains(field.getType())) {
                    continue;
                }
                origin.add(field.getTypeDefinition());
            }
            recursive(origin, book, ignoreProperties);
        }
    }

    public static class GraphQLSchemaBuilder {
        private List<String> parsing = new ArrayList<>();
        private String content;

        public GraphQLSchemaBuilder schema(String schemaInput) {
            this.content = schemaInput;
            return this;
        }

        public GraphQLSchema build() {
            GraphQLSchema schema = new GraphQLSchema();
            schema.content = this.content;

            SchemaParser schemaParser = new SchemaParser();
            TypeDefinitionRegistry registry = schemaParser.parse(this.content);

            loadSchema(registry, schema);

            return schema;
        }

        private void loadSchema(TypeDefinitionRegistry registry, GraphQLSchema schema) {
            GraphQLObjectTypeBuilder queryTypeBuilder = GraphQLObjectType.builder().schema(schema).id("Query").type(GraphQLType.Object);
            GraphQLObjectTypeBuilder mutationTypeBuilder = GraphQLObjectType.builder().schema(schema).id("Mutation").type(GraphQLType.Object);
            GraphQLObjectTypeBuilder subscriptionTypeBuilder = GraphQLObjectType.builder().schema(schema).id("Subscription").type(GraphQLType.Object);

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

            loadFieldDefinitions(subscriptions, registry, schema);

            schema.setQueryType(queryTypeBuilder.build());
            schema.setMutationType(mutationTypeBuilder.build());
            schema.setSubscriptionType(subscriptionTypeBuilder.build());
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
            } else if (definition instanceof InputObjectTypeDefinition) {
                loadInputObjectTypeDefinition((InputObjectTypeDefinition) definition, registry, schema);
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

        private void loadInputObjectTypeDefinition(InputObjectTypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchema schema) {
            if (this.parsing.contains(definition.getName())) {
                System.out.println("循环依赖:" + definition);
                return;
            }
            this.parsing.add(definition.getName());

            loadArguments(definition.getInputValueDefinitions(), registry, schema);

            schema.addType(definition);
            GraphQLObjectType type = schema.getType(definition.getName());
//          TODO:  System.out.println("新增 ModelType : " + definition.getName() + " --- " + type.getBoost());
            this.parsing.remove(definition.getName());
        }

        private void loadObjectTypeDefinition(ObjectTypeDefinition definition, TypeDefinitionRegistry registry, GraphQLSchema schema) {
            if (this.parsing.contains(definition.getName())) {
                System.out.println("循环依赖:" + definition);
                return;
            }
            this.parsing.add(definition.getName());

            loadFieldDefinitions(definition.getFieldDefinitions(), registry, schema);

            schema.addType(definition);
            GraphQLObjectType type = schema.getType(definition.getName());
//          TODO:  System.out.println("新增 ModelType : " + definition.getName() + " --- " + type.getBoost());
            this.parsing.remove(definition.getName());
        }

        private void loadFieldDefinitions(List<FieldDefinition> fields, TypeDefinitionRegistry registry, GraphQLSchema schema) {
            for (FieldDefinition field : fields) {
                String name = typeName(field.getType());
                loadTypeDefinition(registry.getType(name).get(), registry, schema);
                loadArguments(field.getInputValueDefinitions(), registry, schema);
            }
        }

        private void loadArguments(List<InputValueDefinition> inputValueDefinitions, TypeDefinitionRegistry registry, GraphQLSchema schema) {
            for (InputValueDefinition input : inputValueDefinitions) {
                String name = typeName(input.getType());
                loadTypeDefinition(registry.getType(name).get(), registry, schema);
            }
        }

    }

    private void setTypeDefinitionRegistry(TypeDefinitionRegistry registry) {
        this.registry = registry;
    }

    private void setSubscriptionType(GraphQLObjectType type) {
        this.subscriptionType = type;
    }

    private void setMutationType(GraphQLObjectType type) {
        this.mutationType = type;
    }

    private void setQueryType(GraphQLObjectType type) {
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
