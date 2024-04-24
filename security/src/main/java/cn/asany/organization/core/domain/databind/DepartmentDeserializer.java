package cn.asany.organization.core.domain.databind;

import cn.asany.organization.core.domain.Department;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import net.asany.jfantasy.framework.util.common.StringUtil;

/**
 * @author limaofeng
 */
public class DepartmentDeserializer extends JsonDeserializer<Department> {

  @Override
  public Department deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    if (StringUtil.isBlank(jp.getValueAsString())) {
      return null;
    }
    return Department.builder().id(jp.getValueAsLong()).build();
  }
}
