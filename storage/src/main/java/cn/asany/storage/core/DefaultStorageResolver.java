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

import cn.asany.storage.api.IStorageConfig;
import cn.asany.storage.api.Storage;
import cn.asany.storage.api.StorageBuilder;
import cn.asany.storage.data.domain.StorageConfig;
import cn.asany.storage.data.service.StorageService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.asany.jfantasy.framework.jackson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 默认存储器解析器
 *
 * @author limaofeng
 */
public class DefaultStorageResolver implements StorageResolver {

  private StorageService storageService;

  private final Map<String, Storage> storages = new HashMap<>();
  private final List<StorageBuilder<? extends Storage, ? extends IStorageConfig>> builders;

  public DefaultStorageResolver(
      StorageService storageService,
      List<StorageBuilder<? extends Storage, ? extends IStorageConfig>> builders) {
    this.builders = builders;
    this.storageService = storageService;
  }

  @Override
  @Transactional(readOnly = true)
  public Storage resolve(String id) {
    if (storages.containsKey(id)) {
      return storages.get(id);
    }
    StorageConfig config = storageService.get(id);
    return resolve(config.getProperties());
  }

  @Override
  public Storage resolve(IStorageConfig config) {
    for (@SuppressWarnings("rawtypes") StorageBuilder builder : builders) {
      //noinspection unchecked
      if (builder.supports(config.getClass())) {
        @SuppressWarnings("unchecked")
        Storage storage = builder.build(config);
        if (storage != null) {
          storages.put(config.getId(), storage);
          return storage;
        }
      }
    }
    throw new FileStoreException("不能创建 IStorage For " + JSON.serialize(config));
  }

  @SuppressWarnings("unused")
  public void setStorageService(@Autowired StorageService storageService) {
    this.storageService = storageService;
  }
}
