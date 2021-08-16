package cn.asany.shanhai.core.support.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FieldTypeRegistry {

  private Map<String, FieldType> caches = new ConcurrentHashMap<>();

  public void addType(FieldType type) {
    caches.put(type.getId(), type);
  }

  public FieldType getType(String type) {
    return caches.get(type);
  }

  public List<FieldType> types() {
    return caches.values().stream().collect(Collectors.toList());
  }
}
