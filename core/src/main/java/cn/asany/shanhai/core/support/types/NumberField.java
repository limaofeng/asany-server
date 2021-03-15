package cn.asany.shanhai.core.support.types;

import cn.asany.shanhai.core.bean.ModelFieldMetadata;
import cn.asany.shanhai.core.support.DatabaseColumn;
import cn.asany.shanhai.core.support.FieldType;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class NumberField implements FieldType {
    private String id = "Number";
    private String name = "数字";
    private String javaType = Long.class.getName();
    private String graphQLType = "Int";

    @Override
    public String getJavaType(ModelFieldMetadata metadata) {
        return this.javaType;
    }

    @Override
    public DatabaseColumn getColumn(ModelFieldMetadata metadata) {
        return DatabaseColumn.builder().name(metadata.getDatabaseColumnName()).updatable(false).nullable(false).build();
    }
}
