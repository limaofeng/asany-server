package org.jfantasy.framework.util.reflect;

import org.jfantasy.framework.util.common.ClassUtil;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class Property {
    private PropertyDescriptor descriptor;
    private String name;
    private MethodProxy readMethodProxy;
    private MethodProxy writeMethodProxy;
    private Class propertyType;
    private boolean write;
    private boolean read;
    private Map<Class, Annotation> annotationCache = new HashMap<>();

    public Property(PropertyDescriptor descriptor) {
        this.name = descriptor.getName();
        this.readMethodProxy = descriptor.getReadMethod() == null ? null : new MethodProxy(descriptor.getReadMethod());
        this.writeMethodProxy = descriptor.getWriteMethod() == null ? null : new MethodProxy(descriptor.getWriteMethod(), descriptor.getPropertyType());
        this.read = this.readMethodProxy != null;
        this.write = this.writeMethodProxy != null;
        this.propertyType = descriptor.getPropertyType();
        this.descriptor = descriptor;
    }

    public boolean isTransient() {
        Object value = this.descriptor.getValue("transient");
        return (value instanceof Boolean) ? (Boolean) value : false;
    }

    public boolean isWrite() {
        return this.write;
    }

    public boolean isRead() {
        return this.read;
    }

    public Object getValue(Object target) {
        if (!this.read) {
            return null;
        }
        return this.readMethodProxy.invoke(target);
    }

    public void setValue(Object target, Object value) {
        if (!this.write) {
            return;
        }
        this.writeMethodProxy.invoke(target, value);
    }

    public <T> Class<T> getPropertyType() {
        return this.propertyType;
    }

    public String getName() {
        return this.name;
    }

    public <T extends Annotation> T getAnnotation(Class<T> tClass) {
        if (annotationCache.containsKey(tClass)) {
            return (T) annotationCache.get(tClass);
        }
        Annotation annotation = null;
        Class<?> declaringClass = null;
        if (this.isRead()) {
            annotation = this.getReadMethod().getAnnotation(tClass);
            if (annotation == null) {
                declaringClass = this.getReadMethod().getDeclaringClass();
            }
        }
        if (annotation == null && this.isWrite()) {
            annotation = this.getWriteMethod().getAnnotation(tClass);
            if (annotation == null) {
                declaringClass = this.getWriteMethod().getDeclaringClass();
            }
        }
        if (annotation == null) {
            Field field = ClassUtil.getDeclaredField(declaringClass, this.name);
            if (field != null) {
                annotation = field.getAnnotation(tClass);
            }
        }
        annotationCache.put(tClass, annotation);
        return (T) annotation;
    }

    public MethodProxy getReadMethod() {
        return this.readMethodProxy;
    }

    public MethodProxy getWriteMethod() {
        return this.writeMethodProxy;
    }

    public ParameterizedType getGenericType() {
        return (ParameterizedType) this.getReadMethod().getMethod().getGenericReturnType();
    }
}
