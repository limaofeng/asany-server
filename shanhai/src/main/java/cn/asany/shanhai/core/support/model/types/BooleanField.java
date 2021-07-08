package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.bean.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class BooleanField implements FieldType<Boolean, Boolean> {
    private String id = "Boolean";
    private String name = "布尔";
    private String javaType = Boolean.class.getName();
    private String graphQLType = "Boolean";

    @Override
    public java.lang.String getJavaType(ModelFieldMetadata metadata) {
        return this.getJavaType();
    }

    @Override
    public java.lang.String getGraphQLType(ModelFieldMetadata metadata) {
        return this.graphQLType;
    }

}
