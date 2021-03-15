package cn.asany.shanhai.core.support.features;

import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.support.FieldType;
import cn.asany.shanhai.core.support.IModelFeature;
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
        fields.add(ModelField.builder().name("creator").type(FieldType.Number).metadata("CREATOR").build());
        fields.add(ModelField.builder().name("createdAt").type(FieldType.Date).metadata("CREATED_AT").build());
        fields.add(ModelField.builder().name("updator").type(FieldType.Number).metadata("UPDATOR").build());
        fields.add(ModelField.builder().name("updatedAt").type(FieldType.Date).metadata("UPDATED_AT").build());
        return fields;
    }

}
