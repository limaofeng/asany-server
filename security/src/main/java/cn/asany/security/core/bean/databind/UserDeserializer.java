package cn.asany.security.core.bean.databind;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import cn.asany.security.core.bean.User;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.IOException;

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
