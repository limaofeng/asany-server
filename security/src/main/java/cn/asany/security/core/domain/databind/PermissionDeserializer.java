package cn.asany.security.core.domain.databind;

import cn.asany.security.core.domain.Permission;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

/**
 * @author limaofeng@msn.com @ClassName: PermissionDeserializer @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
public class PermissionDeserializer extends JsonDeserializer<Permission> {
  @Override
  public Permission deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    String value = jp.getValueAsString();
    if (value == null) {
      return null;
    }
    return Permission.builder().id(value).build();
  }
}
