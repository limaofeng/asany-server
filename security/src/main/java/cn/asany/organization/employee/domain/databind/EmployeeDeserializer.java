package cn.asany.organization.employee.domain.databind;

import cn.asany.organization.employee.domain.Employee;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class EmployeeDeserializer extends JsonDeserializer<Employee> {

  @Override
  public Employee deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return Employee.builder().id(p.getValueAsLong()).build();
  }
}
