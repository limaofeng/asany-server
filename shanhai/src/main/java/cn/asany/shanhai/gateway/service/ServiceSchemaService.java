package cn.asany.shanhai.gateway.service;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.ModelSaveContext;
import cn.asany.shanhai.gateway.bean.Service;
import cn.asany.shanhai.gateway.bean.ServiceSchema;
import cn.asany.shanhai.gateway.bean.ServiceSchemaVersion;
import cn.asany.shanhai.gateway.bean.ServiceSchemaVersionPatch;
import cn.asany.shanhai.gateway.dao.ServiceSchemaDao;
import cn.asany.shanhai.gateway.dao.ServiceSchemaVersionDao;
import cn.asany.shanhai.gateway.dao.ServiceSchemaVersionPatchDao;
import cn.asany.shanhai.gateway.util.GraphQLField;
import cn.asany.shanhai.gateway.util.GraphQLSchema;
import cn.asany.shanhai.gateway.util.GraphQLObjectType;
import cn.asany.shanhai.gateway.util.SchemaUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private ServiceSchemaDao serviceSchemaDao;
    @Autowired
    private ServiceSchemaVersionDao serviceSchemaVersionDao;
    @Autowired
    private ServiceSchemaVersionPatchDao serviceSchemaVersionPatchDao;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        SchemaUtils.setResourceLoader(resourceLoader);
        SchemaUtils.setRestTemplate(restTemplate);
        SchemaUtils.setObjectMapper(objectMapper);
    }

    @SneakyThrows
    public void loadSchemaForService(Long id) {
        Optional<Service> optionalService = this.serviceRegistryService.getService(id);
        if (!optionalService.isPresent()) {
            return;
        }
        Service service = optionalService.get();
        String url = service.getUrl();

        // 获取远程 Schema 结构
        GraphQLSchema graphQLSchema = SchemaUtils.loadRemoteSchema(url);

        // 如果不存在 ServiceSchema 则立即创建一个
        ServiceSchema schema = service.getSchema();
        if (schema == null) {
            schema = ServiceSchema.builder().service(service).schema("type Query {} type Mutation {} type Subscription {}").versions(new ArrayList<>()).build();
            service.setSchema(schema);
            this.serviceSchemaDao.save(schema);
        }

        // 计算 MD5 防止多次生成重复版本
        String md5 = DigestUtils.md5DigestAsHex(graphQLSchema.getSource().getBytes()).toUpperCase();

        ServiceSchemaVersion previous = schema.latest();
//        if (previous != null && md5.equals(previous.getMd5())) {
//            return;
//        }

        ServiceSchemaVersion.ServiceSchemaVersionBuilder version = ServiceSchemaVersion.builder()
            .md5(md5)
            .body(graphQLSchema.getSource())
            .schema(schema);

        if (previous != null) {
            version.id(previous.getId());
        }

        GraphQLSchema prevSchema = SchemaUtils.loadSchema(schema.getSchema());

        List<ServiceSchemaVersionPatch> patches = SchemaUtils.diff(prevSchema, graphQLSchema);

        this.serviceSchemaVersionDao.save(version.build());
    }


    @Transactional(rollbackFor = RuntimeException.class)
    public void save(GraphQLSchema schema) {
        ModelSaveContext saveContext = ModelSaveContext.newInstance();

        saveContext.setModels(this.modelService.findAll());

        List<GraphQLObjectType> typeDefinitions = schema.getTypeMap().values().stream().collect(Collectors.toList());

        System.out.println(saveContext.getModels().get(0).getMetadata());

        typeDefinitions.add(schema.getMutationType());
        typeDefinitions.add(schema.getQueryType());

        for (GraphQLObjectType definition : typeDefinitions) {
            System.out.println("新增 ModelType : " + definition.getId());

            if (modelService.exists(definition.getId())) {
                continue;
            }

            Model.ModelBuilder builder = Model.builder()
                .code(definition.getId())
                .type(definition.getType().toModelType())
                .name(definition.getDescription());

            for (GraphQLField field : ObjectUtil.defaultValue(definition.getFields(), new ArrayList<GraphQLField>())) {
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
