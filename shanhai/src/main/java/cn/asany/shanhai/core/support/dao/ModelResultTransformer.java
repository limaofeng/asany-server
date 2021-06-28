package cn.asany.shanhai.core.support.dao;

import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import org.hibernate.transform.ResultTransformer;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModelResultTransformer implements ResultTransformer {

    private List<ModelField> fields;
    private OgnlUtil ognlUtil;
    private Map<String, AttributeConverter> converterMap = new ConcurrentHashMap<>();

    public ModelResultTransformer(List<ModelField> fields) {
        FieldTypeRegistry registry = SpringContextUtil.getBeanByType(FieldTypeRegistry.class);
        this.fields = fields;
        for (ModelField field : fields) {
            AttributeConverter type = registry.getType(field.getType().getCode());
            converterMap.put(field.getCode(), type);
        }
        ognlUtil = OgnlUtil.getInstance();
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        return tuple[tuple.length - 1];
    }

    public Object convertToEntity(Object entity) {
        for (Map.Entry<String, AttributeConverter> converterEntry : converterMap.entrySet()) {
            String key = converterEntry.getKey();
            AttributeConverter converter = converterEntry.getValue();
            Object value = ognlUtil.getValue(key, entity);
            Class toType = ClassUtil.getInterfaceGenricType(converter.getClass(), FieldType.class);
            if (value == null || toType.isAssignableFrom(value.getClass())) {
                continue;
            }
            ognlUtil.setValue(key, entity, converter.convertToEntityAttribute(value));
        }
        return entity;
    }

    @Override
    public List transformList(List list) {
        List<Object> result = new ArrayList<Object>(list.size());
        for (Object entity : list) {
            result.add(convertToEntity(entity));
        }
        return result;
    }
}