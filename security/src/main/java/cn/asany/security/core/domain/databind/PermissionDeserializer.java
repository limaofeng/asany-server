package cn.asany.security.core.domain.databind;

import cn.asany.security.core.domain.PermissionStatement;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

/** @author limaofeng@msn.com */
public class PermissionDeserializer extends JsonDeserializer<PermissionStatement> {
  @Override
  public PermissionStatement deserialize(JsonParser jp, DeserializationContext context)
    throws IOException {
    String value = jp.getValueAsString();
    if (value == null) {
      return null;
    }
    return PermissionStatement.builder().build();
  }
}
