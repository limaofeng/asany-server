package cn.asany.security.core.domain.databind;

import cn.asany.security.core.domain.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.jfantasy.framework.util.common.StringUtil;

public class UserDeserializer extends JsonDeserializer<User> {

  @Override
  public User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    Long value = jp.getValueAsLong();
    if (StringUtil.isBlank(value)) {
      return null;
    }
    return User.builder().id(value).build();
  }
}
