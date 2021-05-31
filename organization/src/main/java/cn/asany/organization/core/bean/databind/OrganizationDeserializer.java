package cn.asany.organization.core.bean.databind;

import cn.asany.organization.core.bean.Organization;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.IOException;

/**
 * @author limaofeng
 */
public class OrganizationDeserializer extends JsonDeserializer<Organization> {

    @Override
    public Organization deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (StringUtil.isBlank(jp.getValueAsString())) {
            return null;
        }
        return Organization.builder().id(jp.getValueAsString()).build();
    }

}