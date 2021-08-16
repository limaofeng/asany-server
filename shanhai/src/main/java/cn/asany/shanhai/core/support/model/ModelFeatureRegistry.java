package cn.asany.shanhai.core.support.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModelFeatureRegistry {

  public static Map<String, IModelFeature> caches = new ConcurrentHashMap<>();

  public void add(IModelFeature feature) {
    caches.put(feature.getId(), feature);
  }

  public IModelFeature get(String id) {
    return caches.get(id);
  }
}
