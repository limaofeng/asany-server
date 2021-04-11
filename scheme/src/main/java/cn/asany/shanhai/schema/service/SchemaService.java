package cn.asany.shanhai.schema.service;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.ModelSaveContext;
import cn.asany.shanhai.schema.bean.GraphQLFieldDefinition;
import cn.asany.shanhai.schema.bean.GraphQLSchema;
import cn.asany.shanhai.schema.bean.GraphQLTypeDefinition;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author limaofeng
 */
@Service
public class SchemaService {

    @Autowired
    private ModelService modelService;

    public void loadSchemaForService() {
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void save(GraphQLSchema schema) {
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
