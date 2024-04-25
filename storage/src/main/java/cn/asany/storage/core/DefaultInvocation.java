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
package cn.asany.storage.core;

import cn.asany.storage.api.*;
import java.util.Iterator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultInvocation implements Invocation {

  private final StoragePlugin handler;
  @Getter private final UploadContext context;
  private final Iterator<StoragePlugin> iterator;

  public DefaultInvocation(UploadContext context, Iterator<StoragePlugin> iterator) {
    this.context = context;
    this.iterator = iterator;
    this.handler = iterator.hasNext() ? iterator.next() : null;
  }

  @Override
  public FileObject invoke() throws UploadException {
    if (this.handler == null) {
      if (this.context.getFile().isNoFile()) {
        return null;
      }
      throw new UploadException("handler is null");
    }
    return this.handler.upload(this.context, new DefaultInvocation(this.context, this.iterator));
  }
}
