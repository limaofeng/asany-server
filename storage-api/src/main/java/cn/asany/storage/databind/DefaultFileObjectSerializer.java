package cn.asany.storage.databind;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.dto.SimpleFileObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;

/**
 * 默认文件对象反序列化
 *
 * @author limaofeng
 */
public class DefaultFileObjectSerializer extends JsonDeserializer<FileObject> {

  @Override
  public FileObject deserialize(JsonParser p, DeserializationContext context) throws IOException {
    if (!p.isExpectedStartObjectToken()) {
      return new SimpleFileObject(p.getValueAsString());
    }

    SimpleFileObject object = new SimpleFileObject();
    String key = p.nextFieldName();
    do {
      String value = p.nextTextValue();
      ObjectUtil.setValue(key, object, value);
      key = p.nextFieldName();
    } while (StringUtil.isNotBlank(key));
    return object;
  }
}
