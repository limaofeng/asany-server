package cn.asany.shanhai.schema.service;

import cn.asany.shanhai.schema.TestApplication;
import cn.asany.shanhai.schema.bean.GraphQLSchema;
import graphql.introspection.IntrospectionQuery;
import graphql.introspection.IntrospectionResultToSchema;
import graphql.language.*;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.SchemaPrinter;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.graphql.client.GraphQLClient;
import org.jfantasy.graphql.client.GraphQLResponse;
import org.jfantasy.graphql.client.GraphQLTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static graphql.schema.idl.SchemaPrinter.Options.defaultOptions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class SchemaServiceTest {

    String SCHEMA_PATH = "/Users/limaofeng/Workspace/whir/kuafu/scheme/src/test/resources/schema.text";

    @GraphQLClient
    private GraphQLTemplate graphQLTemplate;

    @Autowired
    private SchemaService schemaService;

    @Test
    @SneakyThrows
    public void testLoadSchemaForService() {
        GraphQLResponse response = graphQLTemplate.post(IntrospectionQuery.INTROSPECTION_QUERY, "IntrospectionQuery");
        assertNotNull(response);
        assertThat(response.isOk()).isTrue();

        Document schemaDefinition = new IntrospectionResultToSchema().createSchemaDefinition(response.get("$.data", HashMap.class));

        SchemaPrinter.Options noDirectivesOption = defaultOptions().includeDirectives(false);

        String result = new SchemaPrinter(noDirectivesOption).print(schemaDefinition);

        System.out.println(result);

        File f = new File(SCHEMA_PATH).getParentFile();
        if (!f.exists() && !f.mkdirs()) {
            throw new IgnoreException("创建文件" + SCHEMA_PATH + "失败");
        }
        try (FileOutputStream fos = new FileOutputStream(SCHEMA_PATH)) {
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(result);
            out.flush();
        } catch (IOException ex) {
            throw new IgnoreException(ex.getMessage());
        }
    }

    private void loadObjectTypeDefinition(InputObjectTypeDefinition definition, Map<String, InputObjectTypeDefinition> typeMap, Map<String, ScalarTypeDefinition> scalars, GraphQLSchema schema) {
        schema.addType(definition);
        for (InputValueDefinition field : definition.getInputValueDefinitions()) {
            if (field.getType() instanceof TypeName) {
                String name = ((TypeName) field.getType()).getName();
                if (schema.getType(name) != null || scalars.containsKey(name)) {
                    continue;
                }
                System.out.println(">>>>>>>>" + definition.getName() + "." + field.getName() + ": " + name);
                loadObjectTypeDefinition(typeMap.get(name), typeMap, scalars, schema);
            }
        }
    }

    private void loadObjectTypeDefinition(ObjectTypeDefinition definition, Map<String, ObjectTypeDefinition> typeMap, Map<String, ScalarTypeDefinition> scalars, GraphQLSchema schema) {
        schema.addType(definition);
        for (FieldDefinition field : definition.getFieldDefinitions()) {
            if (field.getType() instanceof TypeName) {
                String name = ((TypeName) field.getType()).getName();
                if (schema.getType(name) != null || scalars.containsKey(name)) {
                    continue;
                }
                System.out.println(">>>>>>>>" + definition.getName() + "." + field.getName() + ": " + name);
                loadObjectTypeDefinition(typeMap.get(name), typeMap, scalars, schema);
            }
        }
    }

    @Test
    @SneakyThrows
    public void testLoadSchema() {
        GraphQLSchema schema = new GraphQLSchema();
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry registry = schemaParser.parse(FileUtil.readFile(SCHEMA_PATH));

        Map<String, List<String>> models = new HashMap<>();

        Map<String, ObjectTypeDefinition> types = registry.getTypesMap(ObjectTypeDefinition.class);

        schema.addScalars(registry.scalars());

        schema.addEnums(registry.getTypes(EnumTypeDefinition.class));

        schema.addUnions(registry.getTypes(UnionTypeDefinition.class));

        schema.addInterfaces(registry.getTypes(InterfaceTypeDefinition.class));

        for (Map.Entry<String, ObjectTypeDefinition> entry : types.entrySet()) {
            if (entry.getKey().endsWith("Connection") || entry.getKey().endsWith("Edge") || entry.getKey().equals("PageInfo") || entry.getKey().startsWith("GraphQL")) {
                continue;
            }
            loadObjectTypeDefinition(entry.getValue(), types, registry.scalars(), schema);
        }

        Map<String, InputObjectTypeDefinition> inputTypes = registry.getTypesMap(InputObjectTypeDefinition.class);

        for (Map.Entry<String, InputObjectTypeDefinition> entry : inputTypes.entrySet()) {
            loadObjectTypeDefinition(entry.getValue(), inputTypes, registry.scalars(), schema);
        }

        System.out.println("...............");

        schemaService.save(schema);

//        modelService.save();

        // TODO 保存数据

        // TODO 比较差异

        // TODO 增量修改

    }

}