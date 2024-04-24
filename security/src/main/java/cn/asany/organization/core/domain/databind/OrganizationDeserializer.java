package cn.asany.organization.core.domain.databind;

import cn.asany.organization.core.domain.Organization;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import net.asany.jfantasy.framework.util.common.StringUtil;

/**
 * @author limaofeng
 */
public class OrganizationDeserializer extends JsonDeserializer<Organization> {

  @Override
  public Organization deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    if (StringUtil.isBlank(jp.getValueAsString())) {
      return null;
    }
    return Organization.builder().id(jp.getValueAsLong()).build();
  }
}
