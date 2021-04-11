package cn.asany.shanhai.schema.service;

import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.schema.TestApplication;
import cn.asany.shanhai.schema.bean.GraphQLSchema;
import cn.asany.shanhai.schema.bean.GraphQLSchema.GraphQLSchemaBuilder;
import graphql.introspection.IntrospectionQuery;
import graphql.introspection.IntrospectionResultToSchema;
import graphql.language.Document;
import graphql.schema.idl.SchemaPrinter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.graphql.client.GraphQLClient;
import org.jfantasy.graphql.client.GraphQLResponse;
import org.jfantasy.graphql.client.GraphQLTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;
import java.util.HashMap;

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

    @Autowired
    private ModelService modelService;

    @SneakyThrows
    @BeforeEach
    void setUp() {
//        modelService.clear();
    }

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



    @Test
    @SneakyThrows
    public void testLoadSchema() {
        GraphQLSchemaBuilder builder = GraphQLSchema.builder();
        builder.schema(FileUtil.readFile(SCHEMA_PATH));

        GraphQLSchema schema = builder.build();

        System.out.println("...............");

        schemaService.save(schema);

        // TODO 保存数据

        // TODO 比较差异

        // TODO 增量修改

    }

}