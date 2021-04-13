package cn.asany.shanhai.gateway.service;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.gateway.util.GraphQLSchemaDefinition;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class SchemaServiceTest {

    String SCHEMA_PATH = "/Users/limaofeng/Workspace/whir/kuafu/scheme/src/test/resources/schema.text";

    @Autowired
    private ServiceSchemaService schemaService;

    @Autowired
    private ServiceRegistryService serviceRegistryService;

    @SneakyThrows
    @BeforeEach
    void setUp() {
//        modelService.clear();
    }

    @Test
    @SneakyThrows
    public void testLoadSchemaForService() {

        schemaService.loadSchemaForService(1L);

//        GraphQLResponse response = graphQLTemplate.post(IntrospectionQuery.INTROSPECTION_QUERY, "IntrospectionQuery");
//        assertNotNull(response);
//        assertThat(response.isOk()).isTrue();
//
//        Document schemaDefinition = new IntrospectionResultToSchema().createSchemaDefinition(response.get("$.data", HashMap.class));
//
//        SchemaPrinter.Options noDirectivesOption = defaultOptions().includeDirectives(false);
//
//        SchemaPrinter schemaPrinter = new SchemaPrinter(noDirectivesOption);
//
//        String result = schemaPrinter.print(schemaDefinition);

//        System.out.println(result);

//        File f = new File(SCHEMA_PATH).getParentFile();
//        if (!f.exists() && !f.mkdirs()) {
//            throw new IgnoreException("创建文件" + SCHEMA_PATH + "失败");
//        }
//        try (FileOutputStream fos = new FileOutputStream(SCHEMA_PATH)) {
//            Writer out = new OutputStreamWriter(fos, "UTF-8");
//            out.write(result);
//            out.flush();
//        } catch (IOException ex) {
//            throw new IgnoreException(ex.getMessage());
//        }
    }

    @Test
    @SneakyThrows
    public void testLoadSchema() {
        GraphQLSchemaDefinition.GraphQLSchemaBuilder builder = GraphQLSchemaDefinition.builder();
        builder.schema(FileUtil.readFile(SCHEMA_PATH));

        GraphQLSchemaDefinition schema = builder.build();

        System.out.println("...............");

        schemaService.save(schema);

        // TODO 保存数据

        // TODO 比较差异

        // TODO 增量修改

    }

}