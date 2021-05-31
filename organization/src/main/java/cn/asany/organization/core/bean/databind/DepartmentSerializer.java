package cn.asany.organization.core.bean.databind;

import cn.asany.organization.core.bean.Department;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author limaofeng
 */
public class DepartmentSerializer extends JsonSerializer<Department> {

    @Override
    public void serialize(Department department, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (department.getId() != null) {
            jgen.writeNull();
        } else {
            jgen.writeNumber(department.getId());
        }

    }
}