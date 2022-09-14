package cn.asany.shanhai.core.support.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字段类型注册表
 *
 * @author limaofeng
 */
public class FieldTypeRegistry {

  private final Map<String, FieldType<?, ?>> caches = new ConcurrentHashMap<>();

  public void addType(FieldType<?, ?> type) {
    caches.put(type.getId(), type);
  }

  public FieldType getType(String type) {
    return caches.get(type);
  }

  public List<FieldType<?, ?>> types() {
    return new ArrayList<>(caches.values());
  }
}
