package cn.asany.storage.core;

import cn.asany.storage.api.IStorageConfig;
import lombok.Data;

/**
 * 存储配置
 * @author limaofeng
 */
@Data
public abstract class AbstractStorageConfig implements IStorageConfig {

  protected String id;

  @Override
  public String id() {
    return id;
  }
}
