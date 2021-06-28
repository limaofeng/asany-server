package cn.asany.shanhai.core.support.model;

import org.jfantasy.framework.dao.jpa.PropertyFilter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FieldTypeRegistry {

    public static Map<String, FieldType> caches = new ConcurrentHashMap<>();

    public void addType(FieldType type) {
        caches.put(type.getId(), type);
    }

    public FieldType getType(String type) {
        return caches.get(type);
    }

}
