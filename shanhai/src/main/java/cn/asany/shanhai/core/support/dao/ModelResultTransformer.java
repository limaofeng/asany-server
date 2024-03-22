package cn.asany.shanhai.core.support.dao;

import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.AttributeConverter;
import lombok.SneakyThrows;
import org.hibernate.transform.ResultTransformer;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.ognl.OgnlUtil;

public class ModelResultTransformer implements ResultTransformer {

  private final Class<?> entityClass;
  private Collection<ModelField> fields;
  private final OgnlUtil ognlUtil;
  private final Map<String, AttributeConverter<?, ?>> converterMap = new ConcurrentHashMap<>();

  public ModelResultTransformer(Class<?> entityClass, Collection<ModelField> fields) {
    FieldTypeRegistry registry = SpringBeanUtils.getBeanByType(FieldTypeRegistry.class);
    this.entityClass = entityClass;
    this.fields = fields;
    for (ModelField field : fields) {
      AttributeConverter<?, ?> type = registry.getType(field.getType());
      converterMap.put(field.getCode(), type);
    }
    ognlUtil = OgnlUtil.getInstance();
  }

  @Override
  public Object transformTuple(Object[] tuple, String[] aliases) {
    return tuple[tuple.length - 1];
  }

  @SneakyThrows
  public Object convertToEntity(Object object) {
    Object entity = this.entityClass.newInstance();
    for (Map.Entry<String, AttributeConverter<?, ?>> converterEntry : converterMap.entrySet()) {
      String key = converterEntry.getKey();
      AttributeConverter<Object, Object> converter =
          (AttributeConverter<Object, Object>) converterEntry.getValue();
      Object value = ognlUtil.getValue(key, object);
      if (value == null) {
        continue;
      }
      ognlUtil.setValue(key, entity, converter.convertToEntityAttribute(value));
    }
    return entity;
  }

  @Override
  public List<Object> transformList(List list) {
    List<Object> result = new ArrayList<>(list.size());
    for (Object entity : list) {
      result.add(convertToEntity(entity));
    }
    return result;
  }
}
