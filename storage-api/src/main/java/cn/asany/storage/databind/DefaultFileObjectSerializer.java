package cn.asany.storage.databind;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.dto.SimpleFileObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.IOException;

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
    p.nextTextValue();
    String value;
    SimpleFileObject object = new SimpleFileObject();
    do {
      String key = p.getValueAsString();
      value = p.nextTextValue();
      ObjectUtil.setValue(key, object, value);
      value = p.nextTextValue();
    } while (StringUtil.isNotBlank(value));
    return object;
  }
}
