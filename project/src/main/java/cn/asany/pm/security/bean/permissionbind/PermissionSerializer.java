package cn.asany.pm.security.bean.permissionbind;

import cn.asany.pm.security.bean.Permission;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * @author limaofeng@msn.com @ClassName: PermissionSerializer @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
public class PermissionSerializer extends JsonSerializer<Permission> {

  @Override
  public void serialize(Permission value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    gen.writeString(String.valueOf(value.getId()));
  }
}
