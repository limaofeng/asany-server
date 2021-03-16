package cn.asany.shanhai.core.support.model.features;

import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.IModelFeature;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class SystemFieldsFeature implements IModelFeature {
    public static final String ID = "SystemFields";
    private String id = ID;

    @Override
    public List<ModelField> fields() {
        List<ModelField> fields = new ArrayList<>();
        fields.add(ModelField.builder().name("创建人").type(FieldType.Number).metadata("creator", "CREATOR").build());
        fields.add(ModelField.builder().name("创建时间").type(FieldType.Date).metadata("createdAt", "CREATED_AT").build());
        fields.add(ModelField.builder().name("修改人").type(FieldType.Number).metadata("updator", "UPDATOR").build());
        fields.add(ModelField.builder().name("修改时间").type(FieldType.Date).metadata("updatedAt", "UPDATED_AT").build());
        return fields;
    }

}
