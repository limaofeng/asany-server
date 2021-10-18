package cn.asany.organization.core.bean.databind;

import cn.asany.organization.core.bean.Organization;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class OrganizationSerializer extends JsonSerializer<Organization> {
  @Override
  public void serialize(Organization organization, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
    jgen.writeNumber(organization.getId() != null ? organization.getId() : null);
  }
}
