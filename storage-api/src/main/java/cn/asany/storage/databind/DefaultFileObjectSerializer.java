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
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class DefaultFileObjectSerializer extends JsonSerializer<FileObject> {

  @Override
  public void serialize(
      FileObject fileObject, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    jsonGenerator.writeStartObject();
    if (fileObject instanceof SimpleFileObject simple) {
      jsonGenerator.writeStringField("id", simple.getId());
    }
    jsonGenerator.writeStringField("name", fileObject.getName());
    jsonGenerator.writeBooleanField("directory", fileObject.isDirectory());
    jsonGenerator.writeStringField("mimeType", fileObject.getMimeType());
    jsonGenerator.writeNumberField("size", fileObject.getSize());
    jsonGenerator.writeStringField("path", fileObject.getPath());
    jsonGenerator.writeEndObject();
  }
}
