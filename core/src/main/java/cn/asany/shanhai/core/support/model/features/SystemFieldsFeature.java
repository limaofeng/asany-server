package cn.asany.shanhai.core.support.model.features;

import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.IModelFeature;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class SystemFieldsFeature implements IModelFeature {
    public static final String ID = "system-fields";
    private String id = ID;

    @Autowired
    private ModelService modelService;

    @Override
    public List<ModelField> fields() {
        List<ModelField> fields = new ArrayList<>();
        fields.add(ModelField.builder().code("creator").name("创建人").system(true).type(FieldType.Int).build());
        fields.add(ModelField.builder().code("createdAt").name("创建时间").system(true).type(FieldType.Date).build());
        fields.add(ModelField.builder().code("updator").name("修改人").system(true).type(FieldType.Int).build());
        fields.add(ModelField.builder().code("updatedAt").name("修改时间").system(true).type(FieldType.Date).build());
        return fields;
    }

}
