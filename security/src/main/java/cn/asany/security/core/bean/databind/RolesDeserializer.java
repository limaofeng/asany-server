package cn.asany.security.core.bean.databind;

import cn.asany.security.core.bean.Role;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jfantasy.framework.util.common.StringUtil;

/** @author limaofeng */
public class RolesDeserializer extends JsonDeserializer<List<Role>> {

  @Override
  public List<Role> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    if (jp.isExpectedStartArrayToken()) {
      List<Role> roles = new ArrayList<>();
      String value = jp.nextTextValue();
      do {
        if (StringUtil.isNotBlank(value)) {
          roles.add(Role.builder().id(value).build());
        }
        value = jp.nextTextValue();
      } while (StringUtil.isNotBlank(value));
      return roles;
    }
    return null;
  }
}
