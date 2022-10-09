package cn.asany.shanhai.core.support.model;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.Module;
import cn.asany.shanhai.core.service.ModuleService;
import cn.asany.shanhai.core.support.model.types.ObjectField;
import cn.asany.shanhai.core.utils.ModelUtils;
import cn.asany.shanhai.core.utils.TypeNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.jfantasy.framework.spring.SpringBeanUtils;

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

  public FieldType<?, ?> getType(String type) {
    FieldType<?, ?> fieldType = caches.get(type);
    if (fieldType != null) {
      return fieldType;
    }
    Optional<Model> modelOptional = ModelUtils.getInstance().getModelByCode(type);
    return modelOptional
        .map(
            (m) -> {
              Module module =
                  SpringBeanUtils.getBeanByType(ModuleService.class)
                      .findById(m.getModule().getId())
                      .orElse(null);
              assert module != null;
              return new ObjectField(
                  m.getCode(),
                  m.getName(),
                  m.getDescription(),
                  module.getCode() + "." + m.getCode());
            })
        .orElseThrow(() -> new TypeNotFoundException(type));
  }

  public List<FieldType<?, ?>> types() {
    return new ArrayList<>(caches.values());
  }
}
