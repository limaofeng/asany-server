package cn.asany.security.oauth.domain.converter;

import cn.asany.security.oauth.domain.ClientDevice;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

@Converter
public class DeviceConverter implements AttributeConverter<ClientDevice, String> {
  @Override
  public String convertToDatabaseColumn(ClientDevice attribute) {
    if (attribute == null) {
      return null;
    }
    return JSON.serialize(attribute);
  }

  @Override
  public ClientDevice convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return null;
    }
    return JSON.deserialize(dbData, ClientDevice.class);
  }
}
