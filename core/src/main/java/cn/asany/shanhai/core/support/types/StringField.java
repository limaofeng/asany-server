package cn.asany.shanhai.core.support.types;

import cn.asany.shanhai.core.bean.ModelFieldMetadata;
import cn.asany.shanhai.core.support.DatabaseColumn;
import cn.asany.shanhai.core.support.FieldType;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class StringField implements FieldType {
    private String id = "String";
    private String name = "字符串";
    private String javaType = java.lang.String.class.getName();
    private String graphQLType = "String";

    @Override
    public String getJavaType(ModelFieldMetadata metadata) {
        return this.javaType;
    }

    @Override
    public DatabaseColumn getColumn(ModelFieldMetadata metadata) {
        return DatabaseColumn.builder().name(metadata.getDatabaseColumnName()).build();
    }
}
