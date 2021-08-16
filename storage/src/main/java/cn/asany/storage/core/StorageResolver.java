package cn.asany.storage.core;

import cn.asany.storage.api.IStorageConfig;
import cn.asany.storage.api.Storage;

/**
 * StorageResolver
 *
 * @author limaofeng
 */
public interface StorageResolver {

  Storage resolve(String id);

  /**
   * 解析并构建 Storage
   *
   * @param config
   * @return
   */
  Storage resolve(IStorageConfig config);
}
