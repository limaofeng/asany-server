package cn.asany.security.core.bean.databind;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import cn.asany.security.core.bean.Role;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.IOException;

/**
 * @author limaofeng
 */
public class RoleDeserializer extends JsonDeserializer<Role> {

    @Override
    public Role deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String value = jp.getValueAsString();
        if (StringUtil.isBlank(value)) {
            return null;
        }
        return Role.builder().id(value).build();
    }

}
