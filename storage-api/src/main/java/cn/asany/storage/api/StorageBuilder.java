package cn.asany.storage.api;

/**
 * 存储构建器
 *
 * @author limaofeng
 */
public interface StorageBuilder<T extends Storage, C extends IStorageConfig> {

  /**
   * 支持
   *
   * @param clazz 类型
   * @return Boolean
   */
  boolean supports(Class<? extends IStorageConfig> clazz);

  /**
   * 构建方法
   *
   * @param clazz 类型
   * @return T
   */
  T build(C clazz);
}
