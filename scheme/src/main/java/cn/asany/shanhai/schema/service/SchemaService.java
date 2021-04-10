package cn.asany.shanhai.schema.service;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.schema.bean.GraphQLFieldDefinition;
import cn.asany.shanhai.schema.bean.GraphQLSchema;
import cn.asany.shanhai.schema.bean.GraphQLTypeDefinition;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;

@Service
public class SchemaService {

    @Autowired
    private ModelService modelService;

    public void loadSchemaForService() {
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void save(GraphQLSchema schema) {
        for (Map.Entry<String, GraphQLTypeDefinition> entry : schema.getTypeMap().entrySet()) {
            GraphQLTypeDefinition definition = entry.getValue();

//            if(this.modelService.exists(definition.getId())){
//                continue;
//            }

            Model.ModelBuilder builder = Model.builder()
                .code(definition.getId())
                .type(definition.getType().toModelType())
                .name(definition.getDescription());

            for (GraphQLFieldDefinition field : ObjectUtil.defaultValue(definition.getFields(), new ArrayList<GraphQLFieldDefinition>())) {
                builder.field(field.getId(), field.getDescription(), field.getType());
            }

            Model model = builder.build();

//            this.modelService.save(model);
            System.out.println(model);
        }
    }
}
