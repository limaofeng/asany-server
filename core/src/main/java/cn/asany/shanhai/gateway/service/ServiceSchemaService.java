package cn.asany.shanhai.gateway.service;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.ModelSaveContext;
import cn.asany.shanhai.gateway.bean.Service;
import cn.asany.shanhai.gateway.bean.ServiceSchema;
import cn.asany.shanhai.gateway.bean.ServiceSchemaVersion;
import cn.asany.shanhai.gateway.dao.ServiceSchemaDao;
import cn.asany.shanhai.gateway.dao.ServiceSchemaVersionDao;
import cn.asany.shanhai.gateway.dao.ServiceSchemaVersionPatchDao;
import cn.asany.shanhai.gateway.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.introspection.IntrospectionQuery;
import graphql.introspection.IntrospectionResultToSchema;
import graphql.language.Document;
import graphql.schema.idl.SchemaPrinter;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.client.GraphQLResponse;
import org.jfantasy.graphql.client.GraphQLTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static graphql.schema.idl.SchemaPrinter.Options.defaultOptions;

/**
 * @author limaofeng
 */
@org.springframework.stereotype.Service
@Transactional
public class ServiceSchemaService {

    @Autowired
    private ModelService modelService;
    @Autowired
    private ServiceRegistryService serviceRegistryService;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ServiceSchemaDao serviceSchemaDao;
    @Autowired
    private ServiceSchemaVersionDao serviceSchemaVersionDao;
    @Autowired
    private ServiceSchemaVersionPatchDao serviceSchemaVersionPatchDao;

    public GraphQLTemplate createGraphQLClient(String url) {
        return new GraphQLTemplate(resourceLoader, restTemplate, url, objectMapper);
    }

    @SneakyThrows
    public void loadSchemaForService(Long id) {
        Optional<Service> optionalService = this.serviceRegistryService.getService(id);
        if (!optionalService.isPresent()) {
            return;
        }
        Service service = optionalService.get();
        String url = service.getUrl();

        GraphQLTemplate graphQLTemplate = createGraphQLClient(url);

        GraphQLResponse response = graphQLTemplate.post(IntrospectionQuery.INTROSPECTION_QUERY, "IntrospectionQuery");

        Document schemaDefinition = new IntrospectionResultToSchema().createSchemaDefinition(response.get("$.data", HashMap.class));

        SchemaPrinter.Options noDirectivesOption = defaultOptions().includeDirectives(false);

        SchemaPrinter schemaPrinter = new SchemaPrinter(noDirectivesOption);

        String result = schemaPrinter.print(schemaDefinition);

        ServiceSchema schema = service.getSchema();
        if (schema == null) {
            schema = ServiceSchema.builder().service(service).versions(new ArrayList<>()).build();
            service.setSchema(schema);
            this.serviceSchemaDao.save(schema);
        }

        String md5 = DigestUtils.md5DigestAsHex(result.getBytes()).toUpperCase();

        ServiceSchemaVersion previous = schema.latest();
//        if (previous != null && md5.equals(previous.getMd5())) {
//            return;
//        }

        ServiceSchemaVersion.ServiceSchemaVersionBuilder version = ServiceSchemaVersion.builder()
            .md5(md5)
            .body(result)
            .schema(schema);

        if (previous != null) {
            version.id(previous.getId());
        }

        GraphQLSchemaDefinition.GraphQLSchemaBuilder builder = GraphQLSchemaDefinition.builder();
        builder.schema("type Query {} type Mutation {} type Subscription {}");

        GraphQLSchemaDefinition prevSchema = builder.build();

        GraphQLSchemaDefinition.GraphQLSchemaBuilder newBuilder = GraphQLSchemaDefinition.builder();
        newBuilder.schema(result);

        GraphQLSchemaDefinition newSchema = newBuilder.build();

        List<DiffObject> diffs = Equator.diff(prevSchema, newSchema);

        newSchema.dependencies("Query.viewer");

        this.serviceSchemaVersionDao.save(version.build());
    }


    @Transactional(rollbackFor = RuntimeException.class)
    public void save(GraphQLSchemaDefinition schema) {
        ModelSaveContext saveContext = ModelSaveContext.newInstance();

        saveContext.setModels(this.modelService.findAll());

        List<GraphQLTypeDefinition> typeDefinitions = schema.getTypeMap().values().stream().collect(Collectors.toList());

        System.out.println(saveContext.getModels().get(0).getMetadata());

        typeDefinitions.add(schema.getMutationType());
        typeDefinitions.add(schema.getQueryType());

        for (GraphQLTypeDefinition definition : typeDefinitions) {
            System.out.println("新增 ModelType : " + definition.getId());

            if (modelService.exists(definition.getId())) {
                continue;
            }

            Model.ModelBuilder builder = Model.builder()
                .code(definition.getId())
                .type(definition.getType().toModelType())
                .name(definition.getDescription());

            for (GraphQLFieldDefinition field : ObjectUtil.defaultValue(definition.getFields(), new ArrayList<GraphQLFieldDefinition>())) {
                builder.field(field.getId(), field.getDescription(), field.getType());
            }

            Model model = builder.build();

            this.modelService.save(model);
        }

        lazySaveFields(saveContext.getFields());

        ModelSaveContext.clear();
    }

    private void lazySaveFields(List<ModelField> fields) {
        for (ModelField field : fields) {
            this.modelService.save(field);
        }
    }

}
