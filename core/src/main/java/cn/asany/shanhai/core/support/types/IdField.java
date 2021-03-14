package cn.asany.shanhai.core.support.types;

import cn.asany.shanhai.core.bean.ModelFieldMetadata;
import cn.asany.shanhai.core.support.DatabaseColumn;
import cn.asany.shanhai.core.support.FieldType;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class IdField implements FieldType {
    private String id = "Long";
    private String name = "ID";
    private String javaType = Long.class.getName();
    private String graphQLType = "ID";

    @Override
    public String getJavaType(ModelFieldMetadata metadata) {
        return this.javaType;
    }

    @Override
    public DatabaseColumn getColumn(ModelFieldMetadata metadata) {
        return DatabaseColumn.builder().name(metadata.getDatabaseColumnName()).updatable(false).nullable(false).build();
    }
}
