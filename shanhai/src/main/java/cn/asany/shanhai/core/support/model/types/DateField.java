package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.bean.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Component
public class DateField implements FieldType<Date, Timestamp> {
    private String id = "Date";
    private String name = "日期";
    private String javaType = Date.class.getName();
    private String graphQLType = "Date";

    @Override
    public String getJavaType(ModelFieldMetadata metadata) {
        return this.javaType;
    }

    @Override
    public String getGraphQLType(ModelFieldMetadata metadata) {
        return graphQLType;
    }

    @Override
    public Date convertToEntityAttribute(Timestamp dbData) {
        return new Date(dbData.getTime());
    }
}
