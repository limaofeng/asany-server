/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.storage.databind;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.dto.SimpleFileObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;

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
