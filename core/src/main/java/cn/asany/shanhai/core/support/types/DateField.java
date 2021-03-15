package cn.asany.shanhai.core.support.types;

import cn.asany.shanhai.core.bean.ModelFieldMetadata;
import cn.asany.shanhai.core.support.DatabaseColumn;
import cn.asany.shanhai.core.support.FieldType;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
public class DateField implements FieldType {
    private String id = "Date";
    private String name = "日期";
    private String javaType = Date.class.getName();
    private String graphQLType = "Date";

    @Override
    public String getJavaType(ModelFieldMetadata metadata) {
        return this.javaType;
    }

    @Override
    public DatabaseColumn getColumn(ModelFieldMetadata metadata) {
        return DatabaseColumn.builder().name(metadata.getDatabaseColumnName()).updatable(false).nullable(false).build();
    }
}
