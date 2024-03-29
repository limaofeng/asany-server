package cn.asany.pm.screen.bean.screenbind;

import cn.asany.pm.field.bean.Field;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * @author limaofeng@msn.com @ClassName: ProjectTaskSerializer @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
public class IssueFieldSerializer extends JsonSerializer<Field> {

  @Override
  public void serialize(Field value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    gen.writeString(String.valueOf(value.getId()));
  }
}
