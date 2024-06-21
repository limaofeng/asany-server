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
package cn.asany.base.usertype;

import cn.asany.storage.api.FileObject;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractJavaType;

public class FileObjectType extends AbstractJavaType<FileObject> {

  public static final FileObjectType INSTANCE = new FileObjectType();

  public FileObjectType() {
    super(FileObject.class);
  }

  @Override
  public String toString(FileObject value) {
    return JSON.serialize(value);
  }

  @Override
  public FileObject fromString(CharSequence string) {
    if (StringUtil.isBlank(string.toString())) {
      return null;
    }
    return JSON.deserialize(string.toString(), FileObject.class);
  }

  @Override
  public <X> X unwrap(FileObject value, Class<X> type, WrapperOptions options) {
    return (X) toString(value);
  }

  @Override
  public <X> FileObject wrap(X value, WrapperOptions options) {
    return fromString((CharSequence) value);
  }
}
