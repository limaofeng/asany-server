package cn.asany.security.core.domain.databind;

import cn.asany.security.core.domain.Role;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class RoleSerializer extends JsonSerializer<Role> {

  @Override
  public void serialize(Role value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    if (value.getId() == null) {
      gen.writeNull();
    }
    gen.writeNumber(value.getId());
  }
}
